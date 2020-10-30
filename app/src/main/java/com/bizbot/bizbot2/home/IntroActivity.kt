package com.bizbot.bizbot2.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.background.SynchronizationData
import kotlinx.android.synthetic.main.intro.*
import kotlin.concurrent.thread

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)

        /*
        val thread = Thread{
            val synchronizedData  = SynchronizationData(baseContext)
            synchronizedData.SyncData()
        }
        thread.start()

         */


        intro_loading_layout.visibility = View.GONE
        intro_logo_layout.visibility = View.VISIBLE
        //2초후 메인 액티비티로 이동
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        },1000L)


    }

}