package com.bizbot.bizbot2.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bizbot.bizbot2.R
import kotlinx.android.synthetic.main.setting_alert.*

class AlertSetting():AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_alert)

        alert_close_btn.setOnClickListener { finish() }
    }

    override fun onBackPressed() {
        finish()
    }
}