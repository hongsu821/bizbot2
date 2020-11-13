package com.bizbot.bizbot2.setting

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import kotlinx.android.synthetic.main.setting_alert.*

class AlertSetting():AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_alert)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        viewModel.getAllPermit().observe(this, Observer {
           // if(it.alert != null)
            //    alert_check.isChecked = it.alert!!

        })


        //분야
        ArrayAdapter.createFromResource(this,R.array.field,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                large_spinner.adapter = arrayAdapter}
        large_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var id : Int? = null
                when(p2){
                    0->id = R.array.select_default
                    1->id = R.array.management
                    2->id = R.array.finance
                    3->id = R.array.technique
                    4->id = R.array.domestic_demand
                    5->id = R.array.shared_growth
                    6->id = R.array.export
                    7->id = R.array.employee
                    8->id = R.array.system
                    9->id = R.array.startup
                }
                if(id!=null)
                    ArrayAdapter.createFromResource(baseContext,id,R.layout.setting_spinner_item)
                        .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                            medium_spinner.adapter = arrayAdapter}

            }
        }

        //뒤로가기 버튼 클릭시
        alert_close_btn.setOnClickListener { finish() }
    }

    override fun onBackPressed() {
        finish()
    }
}