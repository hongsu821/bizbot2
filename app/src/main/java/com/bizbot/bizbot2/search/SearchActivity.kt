package com.bizbot.bizbot2.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bizbot.bizbot2.R
import kotlinx.android.synthetic.main.search_activity.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        search_close_btn.setOnClickListener {
            finish()
        }
    }
}