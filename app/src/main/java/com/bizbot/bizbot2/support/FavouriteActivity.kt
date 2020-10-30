package com.bizbot.bizbot2.support

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bizbot.bizbot2.R
import kotlinx.android.synthetic.main.favourite_activity.*

class FavouriteActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favourite_activity)

        favourite_close_btn.setOnClickListener {
            finish()
        }
    }
}