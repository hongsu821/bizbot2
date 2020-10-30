package com.bizbot.bizbot2.partner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bizbot.bizbot2.R
import kotlinx.android.synthetic.main.partner_activity.*

class PartnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity)

        partner_close_btn.setOnClickListener {
            finish()
        }
    }
}