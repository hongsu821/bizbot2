package com.bizbot.bizbot2.setting

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.background.LoadDataJobService
import com.bizbot.bizbot2.home.IntroActivity
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.PermitModel
import kotlinx.android.synthetic.main.setting_alert.*
import kotlinx.android.synthetic.main.setting_myinfo.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertSetting():AppCompatActivity() {
    companion object{
        val TAG = "AlertSetting"
    }

    private var permit: PermitModel? = null
    var arrayID:Int = R.array.select_default
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_alert)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        viewModel.getAllPermit().observe(this, Observer { permitModel ->
            this.permit = permitModel

            //알림 설정
            permit?.alert?.let { it->
                alert_check.isChecked = it
            }

            //사업 소재지 출력
            permit?.areaNum?.let {
                area_spinner.setSelection(it)
                arrayID = setArray(it)
                setSpinner(arrayID)
            }
            permit?.cityNum?.let {city->
                city_spinner.setSelection(city)
            }

            //알림 받을 키워드
            permit?.keyword?.let{it ->
                val keyword = it.split("@")
                for(i in 0..keyword.size){
                    when(i){
                        0-> keyword1_et.setText(keyword[i])
                        1-> keyword2_et.setText(keyword[i])
                        2-> keyword3_et.setText(keyword[i])
                    }
                }
            }
        })

        //알림 체크
        alert_check.setOnCheckedChangeListener { _, isChecked ->
            permit?.alert = isChecked
        }

        //지역 스피너
        ArrayAdapter.createFromResource(this, R.array.area_array, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                area_spinner.adapter = arrayAdapter}
        ArrayAdapter.createFromResource(baseContext, arrayID, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                city_spinner.adapter = arrayAdapter
            }
        area_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                permit?.areaNum = p2
                arrayID = setArray(p2)
                setSpinner(arrayID)
            }
        }
        setSpinner(arrayID)
        city_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                permit?.cityNum = p2
            }
        }




        //저장하기 버튼 클릭시
        alert_save.setOnClickListener {
            //알림 체크 여부
            permit?.alert = alert_check.isChecked

            //알림 체크한 시간(동기화 시간)
            val syncDate = Date(System.currentTimeMillis())
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            permit?.syncTime = simpleDateFormat.format(syncDate)

            //지역
            permit?.area = area_spinner.selectedItem.toString()
            permit?.city = city_spinner.selectedItem.toString()

            //키워드
            var line = ""
            line += "${keyword1_et.text}@"
            line += "${keyword2_et.text}@"
            line += "${keyword3_et.text}"
            permit?.keyword = line

            //jobSchedule
            val serviceComponent = ComponentName(this, LoadDataJobService::class.java)
            val js = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo = JobInfo.Builder(IntroActivity.JOB_ID,serviceComponent)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE) //와이파이일때
                .setPeriodic(TimeUnit.MINUTES.toMillis(30)) //30분마다
                .build()

            if(permit?.alert!!){
                Toast.makeText(this,"광고성 메시지 수신에 동의 하였습니다.(${permit?.syncTime})",Toast.LENGTH_SHORT).show()
                js.schedule(jobInfo)
            }
            else {
                Toast.makeText(this, "광고성 메시지 수신에 거절 하였습니다.(${permit?.syncTime})", Toast.LENGTH_SHORT).show()
                js.cancel(IntroActivity.JOB_ID)
            }

            viewModel.updatePermit(permit!!)
        }

        //뒤로가기 버튼 클릭시
        alert_close_btn.setOnClickListener { finish() }
    }

    fun setSpinner(arrayID:Int){
        ArrayAdapter.createFromResource(baseContext, arrayID, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                city_spinner.adapter = arrayAdapter
            }
    }

    fun setArray(position : Int):Int{
        var arrayId = 0
        when(position){
            0-> arrayId = R.array.select_default
            1-> arrayId = R.array.Seoul
            2-> arrayId = R.array.Busan
            3-> arrayId = R.array.Dae_gu
            4-> arrayId = R.array.Incheon
            5-> arrayId = R.array.Gwangju
            6-> arrayId = R.array.Daejeon
            7-> arrayId = R.array.Ulsan
            8-> arrayId = R.array.Sejong
            9-> arrayId = R.array.Gangwon_do
            10-> arrayId = R.array.Gyeonggi_do
            11-> arrayId = R.array.Chung_cheong_bukdo
            12-> arrayId = R.array.Chungcheongnam_do
            13-> arrayId = R.array.Jeollabuk_do
            14-> arrayId = R.array.Jeollanam_do
            15-> arrayId = R.array.Gyeongsangbuk_do
            16-> arrayId = R.array.Gyeongsangnam_do
            17-> arrayId = R.array.Jeju
        }
        return arrayId
    }

    override fun onBackPressed() {
        finish()
    }
}