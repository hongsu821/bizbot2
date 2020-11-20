package com.bizbot.bizbot2.setting

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
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.PermitModel
import kotlinx.android.synthetic.main.setting_alert.*
import kotlinx.android.synthetic.main.setting_myinfo.*
import java.text.SimpleDateFormat
import java.util.*

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

            permit?.alert?.let { it->
                alert_check.isChecked = it
            }

            permit?.field?.let { it ->
                large_spinner.setSelection(it)
                setArray(it)
                large_spinner.setSelection(it)
            }
            permit?.subclass?. let{it ->
                medium_spinner.setSelection(it)
            }
            permit?.keyword?.let{it ->
                val keyword = it.split("@")
                keyword1_et.setText(keyword[0])
                keyword2_et.setText(keyword[1])
                keyword3_et.setText(keyword[2])
            }

        })

        alert_test.setOnClickListener {
            Log.d(TAG, "onCreate: permit = ${permit?.id}, ${permit?.alert}, ${permit?.syncTime}, ${permit?.keyword}, ${permit?.field}, ${permit?.subclass}")

        }

        //알림 체크
        alert_check.setOnCheckedChangeListener { compoundButton, isChecked ->
            permit?.alert = isChecked
        }



        //분야
        ArrayAdapter.createFromResource(this,R.array.field,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                large_spinner.adapter = arrayAdapter}
        ArrayAdapter.createFromResource(baseContext,arrayID,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                medium_spinner.adapter = arrayAdapter}
        large_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                arrayID = setArray(p2)
                setSpinner(arrayID)
                permit?.field = p2
            }
        }
        medium_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                permit?.subclass = p2
            }

        }

        //키워드
        var line = ""
        line += "${keyword1_et.text}@"
        line += "${keyword2_et.text}@"
        line += "${keyword3_et.text}@"


        alert_save.setOnClickListener {
            permit?.alert = alert_check.isChecked
            //알림 체크한 시간(동기화 시간)
            val syncDate = Date(System.currentTimeMillis())
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            permit?.syncTime = simpleDateFormat.format(syncDate)

            permit?.keyword = line

            if(permit?.alert!!)
                Toast.makeText(this,"광고성 메시지 수신에 동의 하였습니다.(${permit?.syncTime})",Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this,"광고성 메시지 수신에 거절 하였습니다.(${permit?.syncTime})",Toast.LENGTH_SHORT).show()

            viewModel.insertPermit(permit!!)
        }

        //뒤로가기 버튼 클릭시
        alert_close_btn.setOnClickListener { finish() }
    }

    fun setSpinner(arrayID:Int){
        ArrayAdapter.createFromResource(baseContext, arrayID, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                medium_spinner.adapter = arrayAdapter
            }
    }

    fun setArray(position: Int):Int{
        var arrayid = 0
        when(position){
            0->arrayid = R.array.select_default
            1->arrayid = R.array.management
            2->arrayid = R.array.finance
            3->arrayid = R.array.technique
            4->arrayid = R.array.domestic_demand
            5->arrayid = R.array.shared_growth
            6->arrayid = R.array.export
            7->arrayid = R.array.employee
            8->arrayid = R.array.system
            9->arrayid = R.array.startup
        }
        return arrayid
    }

    override fun onBackPressed() {
        finish()
    }
}