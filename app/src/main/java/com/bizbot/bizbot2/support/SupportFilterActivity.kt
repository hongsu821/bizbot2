package com.bizbot.bizbot2.support

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bizbot.bizbot2.R
import kotlinx.android.synthetic.main.support_filter.*
import java.util.*
import kotlin.collections.ArrayList

class SupportFilterActivity: AppCompatActivity(){
    var areaItem = ""
    var fieldItem = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_filter)

        val pagerAdapter = CustomAdapter(supportFragmentManager)
        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.getTabAt(0)?.text = "지역"
        tab_layout.getTabAt(1)?.text = "분야"


        val intent = Intent(this,SupportActivity::class.java)
        //적용하기 버튼
        filter_ok_btn.setOnClickListener {
            pagerAdapter.getItems()

            intent.putExtra("area",areaItem)
            intent.putExtra("field",fieldItem)
            startActivity(intent)
            finish()
        }

        //나가기 버튼
        filter_close_btn.setOnClickListener {
            startActivity(Intent(this,SupportActivity::class.java))
            finish()
        }

    }

    inner class CustomAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fList:ArrayList<Fragment> = ArrayList()
        init {
            fList.add(FilterAreaFragment())
            fList.add(FilterFieldFragment())
        }

        override fun getItem(position: Int): Fragment {
            return fList[position]
        }

        override fun getCount(): Int {
            return fList.size
        }

        fun getItems(){
            areaItem = fList[0].arguments?.getString("area").toString()
            fieldItem = fList[1].arguments?.getString("field").toString()
        }

    }

    class FilterHandler(looper: Looper):Handler(looper){
        override fun handleMessage(msg: Message) {
        }
    }



}