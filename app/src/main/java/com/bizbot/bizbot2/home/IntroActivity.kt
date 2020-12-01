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
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class IntroActivity : AppCompatActivity() {
    companion object{
        private val TAG = "IntroActivity"
        const val JOB_ID = 1001
    }

    lateinit var introHandler:Handler
    private val msg = Message()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)
        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        //db위치
        val dbPath = baseContext.getDatabasePath("app_db")

        if(!dbPath.exists()){ //db가 없다면
            //알림 설정 팝업
            customDialog(this)
        }else{//db가 있다면
            intro_init_layout.visibility = View.GONE
            viewModel.getAllPermit().observe(this, androidx.lifecycle.Observer {
                if(it.alert!!) //알림 설정시
                    nextActivity() //바로 다음 엑티비티
                else{//알림 설정 해제시
                    sync() //데이터 동기화
                    nextActivity() //다음 엑티비티
                }
            })
        }


        //handler
        introHandler = Handler(Looper.myLooper()!!){
            //arg1 = initData 에서 받음
            //agr2 = initUserInfo 에서 받음
            if(it.arg1 == it.arg2){
                nextActivity()
            }
            true
        }

    }

    //알림 설정 다이얼로그
    private fun customDialog(context: Context){
        intro_logo_layout.visibility = View.GONE

        val permitModel = PermitModel(0,false,null,"@@@",null,null,null,null)

        //다이얼로그설정
        val builder = AlertDialog.Builder(context)
        val mView = LayoutInflater.from(context).inflate(R.layout.intro_dialog_alert,null)
        val yesBtn = mView.findViewById<Button>(R.id.dialog_yes_btn)
        val noBtn = mView.findViewById<Button>(R.id.dialog_no_btn)

        builder.setView(mView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        //동기화 시간
        val syncDate = Date(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        permitModel.syncTime = simpleDateFormat.format(syncDate)

        //jobSchedule 설정
        val js = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceComponent = ComponentName(this, LoadDataJobService::class.java)
        val jobInfo = JobInfo.Builder(JOB_ID,serviceComponent)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE) //와이파이일때
            .setPeriodic(TimeUnit.MINUTES.toMillis(30)) //30분마다
            .build()

        yesBtn.setOnClickListener {
            Toast.makeText(context,"비즈봇의 광고성 정보 수신동의가 처리되었습니다.(${permitModel.syncTime})",Toast.LENGTH_SHORT).show()
            permitModel.alert = true
            DB_IO(permitModel)
            //jobschedule 반복
            js.schedule(jobInfo)

            initUserInfo()
            dialog.dismiss()
        }

        noBtn.setOnClickListener {
            Toast.makeText(context,"비즈봇의 광고성 정보 수신거절이 처리되었습니다.(${permitModel.syncTime})",Toast.LENGTH_SHORT).show()
            permitModel.alert = false
            DB_IO(permitModel)
            //jobschedule 해제
            js.cancel(JOB_ID)

            initUserInfo()
            dialog.dismiss()
        }

    }

    //초기 데이터
    private fun initUserInfo() {
        //서버에서 데이터 받아오기
        GlobalScope.launch(Dispatchers.IO) {
            val initData = InitData(baseContext)
            msg.arg1 = initData.init()
            introHandler.sendMessage(msg)
        }
        //로딩 레이아웃
        intro_loading_layout.visibility = View.GONE
        //기본 정보 입력하는 레이아웃
        intro_init_layout.visibility = View.VISIBLE

        //유저 정보
        val userModel = UserModel(0,null,null,null,null,
            null,null,null,null,null,null,null)
        //필수 항목 체크 여부 확인용
        var businessTypeNum:Int? = null
        var genderNum:Int? = null

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        //사업소재지
        var areaID:Int? = null
        var cityID:Int? = null
        var areaArrayID = R.array.select_default
        ArrayAdapter.createFromResource(this, R.array.area_array, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                intro_area_spinner.adapter = arrayAdapter}
        ArrayAdapter.createFromResource(baseContext, areaArrayID, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                intro_city_spinner.adapter = arrayAdapter
            }
        intro_area_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                areaID = p2
                when(p2){
                    0-> areaArrayID = R.array.select_default
                    1-> areaArrayID = R.array.Seoul
                    2-> areaArrayID = R.array.Busan
                    3-> areaArrayID = R.array.Dae_gu
                    4-> areaArrayID = R.array.Incheon
                    5-> areaArrayID = R.array.Gwangju
                    6-> areaArrayID = R.array.Daejeon
                    7-> areaArrayID = R.array.Ulsan
                    8-> areaArrayID = R.array.Sejong
                    9-> areaArrayID = R.array.Gangwon_do
                    10-> areaArrayID = R.array.Gyeonggi_do
                    11-> areaArrayID = R.array.Chung_cheong_bukdo
                    12-> areaArrayID = R.array.Chungcheongnam_do
                    13-> areaArrayID = R.array.Jeollabuk_do
                    14-> areaArrayID = R.array.Jeollanam_do
                    15-> areaArrayID = R.array.Gyeongsangbuk_do
                    16-> areaArrayID = R.array.Gyeongsangnam_do
                    17-> areaArrayID = R.array.Jeju
                }
                setCitySpinner(areaArrayID)
            }
        }
        setCitySpinner(areaArrayID)
        intro_city_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cityID = p2
            }
        }
        val area = intro_area_spinner.selectedItem.toString()
        val city = intro_city_spinner.selectedItem.toString()


        //사업자 유형
        intro_business_type.setOnCheckedChangeListener { _, i ->
            businessTypeNum= i
            when(i){
                R.id.intro_corporation_business->userModel.businessType = intro_corporation_business.text.toString()
                R.id.intro_private_business->userModel.businessType = intro_private_business.text.toString()
                R.id.intro_reserve_business->userModel.businessType = intro_reserve_business.text.toString()
            }
        }

        //오늘날짜
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        //성별
        intro_gender_type.setOnCheckedChangeListener { _, i ->
            genderNum = i
            when(i){
                R.id.intro_male -> userModel.gender = intro_male.text.toString()
                R.id.intro_female->userModel.gender = intro_female.text.toString()
            }
        }

        //생년월일
        intro_birth_layout.setOnClickListener {
            val dpd = DatePickerDialog(this, R.style.spinner_date_picker,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    userModel.birth = "$year.${monthOfYear + 1}.$dayOfMonth"
                    intro_birth_tx.text = userModel.birth
                }, year, month, day
            )
            dpd.show()

        }

        //업종
        ArrayAdapter.createFromResource(this,R.array.category_of_business,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                intro_category_of_business_spinner.adapter = arrayAdapter}
        intro_category_of_business_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userModel.businessCategoryNum = p2
            }
        }

        //지원받고싶은 분야
        var arrayID = R.array.select_default
        ArrayAdapter.createFromResource(this,R.array.field,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                intro_large_spinner.adapter = arrayAdapter}
        ArrayAdapter.createFromResource(baseContext,arrayID,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                intro_medium_spinner.adapter = arrayAdapter}
        intro_large_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userModel?.fieldNum = p2
                when(p2){
                    0->arrayID = R.array.select_default
                    1->arrayID = R.array.management
                    2->arrayID = R.array.finance
                    3->arrayID = R.array.technique
                    4->arrayID = R.array.domestic_demand
                    5->arrayID = R.array.shared_growth
                    6->arrayID = R.array.export
                    7->arrayID = R.array.employee
                    8->arrayID = R.array.system
                    9->arrayID = R.array.startup
                }
                setMediumSpinner(arrayID)
            }
        }
        //소분류
        setMediumSpinner(arrayID)
        intro_medium_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userModel?.subclassNum = p2
            }
        }

        //다음 버튼 클릭시
        intro_next_btn.setOnClickListener {
            //사업체명
            userModel.businessName = intro_business_name_et.text.toString()
            //대표자 이름
            userModel.name = intro_ceo_name_et.text.toString()
            //업종
            userModel.businessCategory = intro_category_of_business_spinner.selectedItem.toString()
            //사업소재지
            userModel.field = intro_large_spinner.selectedItem.toString()
            userModel.subclass = intro_medium_spinner.selectedItem.toString()
            //db에 저장
            viewModel.insertUser(userModel)

            if (businessTypeNum != null && genderNum != null && userModel.birth != null && areaID != 0 && areaID != null){
                //사업소재지
                viewModel.setArea(area, areaID!!, city, cityID!!)
                //로딩 레이아웃
                intro_loading_layout.visibility = View.VISIBLE
                //정보 입력 레이아웃
                intro_init_layout.visibility = View.GONE

                msg.arg2 = 1
            }
            else
                Toast.makeText(this,"필수 항목을 작성해 주세요!",Toast.LENGTH_SHORT).show()

        }
    }

    //스피너 설정
    private fun setMediumSpinner(arrayID: Int){
        ArrayAdapter.createFromResource(baseContext, arrayID, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                intro_medium_spinner.adapter = arrayAdapter
            }
    }
    private fun setCitySpinner(arrayID: Int){
        ArrayAdapter.createFromResource(baseContext, arrayID, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                intro_city_spinner.adapter = arrayAdapter
            }
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
            synchronizationData.syncData()
        }
    }

}