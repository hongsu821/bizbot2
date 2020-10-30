package com.bizbot.bizbot2.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.partner.PartnerActivity
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.search.SearchActivity
import com.bizbot.bizbot2.setting.SettingActivity
import com.bizbot.bizbot2.support.FavouriteActivity
import com.bizbot.bizbot2.support.SupportActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var viewAdapter: RecyclerView.Adapter<AdvertisingAdapter.ViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        viewModel.getAllSupport().observe(this, Observer {
            viewAdapter = AdvertisingAdapter(baseContext,it as ArrayList<SupportModel>)
            ad_list_rv.adapter = viewAdapter
        })

        viewManager = LinearLayoutManager(this)

        ad_list_rv.layoutManager = viewManager
        ad_list_rv.setHasFixedSize(true)


        //하단 네비게이션바
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        //검색 버튼
        search_bar.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }
        //설정 버튼
        setting_btn.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.support->{
                startActivity(Intent(this, SupportActivity::class.java))
            }
            R.id.favourite->{
                startActivity(Intent(this, FavouriteActivity::class.java))
            }
            R.id.partner->{
                startActivity(Intent(this, PartnerActivity::class.java))
            }
        }
        return true
    }


}