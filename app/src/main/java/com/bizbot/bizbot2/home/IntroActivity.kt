package com.bizbot.bizbot2.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.background.InitData
import com.bizbot.bizbot2.background.LoadDataJobService
import com.bizbot.bizbot2.background.SynchronizationData
import com.bizbot.bizbot2.room.AppDatabase
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.PermitModel
import com.bizbot.bizbot2.room.model.UserModel
import kotlinx.android.synthetic.main.intro.*
import kotlinx.android.synthetic.main.setting_myinfo.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class IntroActivity : AppCompatActivity() {
    companion object{
        private val TAG = "IntroActivity"
        val JOB_ID = 1001
    }

    lateinit var introHandler:Handler
    val msg = Message()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)
        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        //db위치
        val dbPath = baseContext.getDatabasePath("app_db")

        if(!dbPath.exists()){ //db가 없다면
            initData(this)
        }else{//db가 있다면
            init_layout.visibility = View.GONE
            viewModel.getAllPermit().observe(this, androidx.lifecycle.Observer {
                if(it.alert!!) //알림 설정시
                    nextActivity() //바로 다음 엑티비티
                else{//알림 설정 해제시
                    sync() //데이터 동기화
                    nextActivity() //다음 엑티비티
                }
            })
        }

        introHandler = Handler(Looper.myLooper()!!){
            if(it.what == 1){
                nextActivity()
            }else{
                Toast.makeText(this,"에러 발생! 네트워크 설정을 확인해 주세요.",Toast.LENGTH_SHORT).show()
                exitProcess(0)
            }
            true
        }

    }

    //초기 데이터
    private fun initData(context: Context) {
        //기본 정보 입력하는 레이아웃
        init_layout.visibility = View.VISIBLE
        //로딩 레이아웃
        intro_loading_layout.visibility = View.GONE
        //유저 정보
        initUserData()
        //다음 버튼 클릭시
        intro_next_btn.setOnClickListener {
            //알림 설정 팝업
            customDialog(context)
            //서버에서 데이터 받아오기
            GlobalScope.launch(Dispatchers.IO) {
                intro_loading_layout.visibility = View.VISIBLE
                val initData = InitData(baseContext)
                msg.what = initData.init()
                introHandler.sendMessage(msg)
            }
        }
    }

    //알림 설정 다이얼로그
    private fun customDialog(context: Context){
        val permitModel = PermitModel(0,false,null,"@@@",0,0)

        val builder = AlertDialog.Builder(context)
        val mView = LayoutInflater.from(context).inflate(R.layout.intro_dialog_layout,null)
        val yesBtn = mView.findViewById<Button>(R.id.dialog_yes_btn)
        val noBtn = mView.findViewById<Button>(R.id.dialog_no_btn)

        builder.setView(mView)
        val dialog = builder.create()
        dialog.show()

        val syncDate = Date(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        permitModel.syncTime = simpleDateFormat.format(syncDate)

        val js = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        yesBtn.setOnClickListener {
            Toast.makeText(context,"비즈봇의 광고성 정보 수신동의가 처리되었습니다.(${permitModel.syncTime})",Toast.LENGTH_SHORT).show()
            permitModel.alert = true
            DB_IO(permitModel)
            //jobschedule 반복
            val serviceComponent = ComponentName(this, LoadDataJobService::class.java)
            val jobInfo = JobInfo.Builder(JOB_ID,serviceComponent)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE) //와이파이일때
                .setPeriodic(TimeUnit.MINUTES.toMillis(30)) //30분마다
                .build()
            js.schedule(jobInfo)

            dialog.dismiss()
        }

        noBtn.setOnClickListener {
            Toast.makeText(context,"비즈봇의 광고성 정보 수신거절이 처리되었습니다.(${permitModel.syncTime})",Toast.LENGTH_SHORT).show()
            permitModel.alert = false
            DB_IO(permitModel)
            //jobschedule 해제
            js.cancel(JOB_ID)

            dialog.dismiss()
        }

    }

    //내정보 초기 설정
    private fun initUserData(){
        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val userModel = UserModel(0,null,null,null,null,null,null,null,null,null)

        //사업자 유형
        intro_business_type.setOnCheckedChangeListener { radioGroup, i ->
            userModel.businessType = i
        }
        //사업체명
        userModel.businessName = intro_business_name_et.text.toString()

        //오늘날짜
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        //창립일
        intro_establishment_layout.setOnClickListener {
            val dpd = DatePickerDialog(this, R.style.spinner_date_picker,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    userModel.establishment = "$year.${monthOfYear + 1}.$dayOfMonth"
                    intro_establishment_tv.text = userModel.establishment
                }, year, month, day
            )
            dpd.show()
        }

        //대표자 이름
        userModel.name = intro_ceo_name_et.text.toString()

        //생년월일
        intro_birth_layout.setOnClickListener {
            intro_birth_tx
        }

        //업종
        intro_category_of_business_spinner
        //지역
        intro_area_spinner
        //도시
        intro_city_spinner

    }
    //알림설정, 동기화 시간 저장
    private fun DB_IO(permitModel: PermitModel){
        GlobalScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(baseContext,AppDatabase::class.java,"app_db").build()
            db.permitDAO().insert(permitModel)
        }
    }

    //다음 엑티비티로 이동
    private fun nextActivity(){
        intro_loading_layout.visibility = View.GONE
        intro_logo_layout.visibility = View.VISIBLE
        //1.5초후 메인 액티비티로 이동
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        },1500L)
    }

    //백그라운드로 데이터 받기
    private fun sync(){
        GlobalScope.launch(Dispatchers.IO) {
            val synchronizationData = SynchronizationData(baseContext)
            synchronizationData.SyncData()
        }
    }

}