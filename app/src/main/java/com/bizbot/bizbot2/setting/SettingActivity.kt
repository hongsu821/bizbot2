package com.bizbot.bizbot2.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import kotlinx.android.synthetic.main.setting_activity.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        viewModel.getUserName().observe(this, Observer {
            if(it == "")
                user_name.text = "대표자 이름"
            else
                user_name.text = it
        })

        //내정보 설정 버튼 클릭시
        user_info_setting_btn.setOnClickListener { startActivity(Intent(this,MyInfoSetting::class.java)) }
        //알람설정 버튼 클릭시
        alert_setting_btn.setOnClickListener { startActivity(Intent(this,AlertSetting::class.java)) }
        //나가기 버튼
        setting_close_btn.setOnClickListener {  finish()  }
    }

    override fun onBackPressed() {
        finish()
    }
}