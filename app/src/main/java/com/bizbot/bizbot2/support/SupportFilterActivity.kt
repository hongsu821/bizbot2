package com.bizbot.bizbot2.support

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bizbot.bizbot2.R
import kotlinx.android.synthetic.main.support_filter.*
import kotlinx.android.synthetic.main.support_filter_arear_fragment.*


class SupportFilterActivity: AppCompatActivity() {
    var areaItem = ""
    var fieldItem = ""

    var viewList = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_filter)

        viewList.add(layoutInflater.inflate(R.layout.support_filter_arear_fragment,null))
        viewList.add(layoutInflater.inflate(R.layout.support_filter_field_fragment,null))

        view_pager.adapter = CustomAdapter(supportFragmentManager)

        tab_layout.setupWithViewPager(view_pager)

        tab_layout.getTabAt(0)?.text = "지역"
        tab_layout.getTabAt(1)?.text = "분야"


        val intent = Intent(this,SupportActivity::class.java)

        //적용하기 버튼
        filter_ok_btn.setOnClickListener {
            startActivity(intent)
            finish()
        }


        //나가기 버튼
        filter_close_btn.setOnClickListener {
            startActivity(Intent(this,SupportActivity::class.java))
            finish()
        }



    }

    inner class CustomAdapter:FragmentPagerAdapter{

        private val fList:ArrayList<Fragment> = ArrayList()

        constructor(fragmentManager: FragmentManager) : super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
            fList.add(FilterAreaFragment())
            fList.add(FilterFieldFragment())
        }

        override fun getItem(position: Int): Fragment {
            return fList[position]
        }

        override fun getCount(): Int {
            return fList.size
        }


    }

    class CAHandler(looper: Looper):Handler(looper){
        override fun handleMessage(msg: Message) {
        }
    }


}