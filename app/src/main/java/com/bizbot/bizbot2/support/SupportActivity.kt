package com.bizbot.bizbot2.support

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.search.SearchActivity
import kotlinx.android.synthetic.main.support_activity.*

class SupportActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_activity)

        //선택한 분야 가져오기
        var fieldWord = intent.getStringExtra("field")
        if(fieldWord == null || fieldWord == "")
            fieldWord = "전체"
        //선택한 지역 출력
        var areaWord = intent.getStringExtra("area")
        if(areaWord == null || areaWord == "")
            areaWord = "전체"
        area_state.text = areaWord

        //지원사업 리사이클러뷰
        val viewManager = LinearLayoutManager(this)
        support_rv.layoutManager = viewManager
        support_rv.setHasFixedSize(true)
        val supportAdapter = SupportListAdapter(baseContext,this,areaWord,fieldWord)

        //데이터 가져오기
        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        viewModel.getAllSupport().observe(this, Observer {
            supportAdapter.setList(it as ArrayList<SupportModel>)
            support_rv.adapter = supportAdapter
            progressBar.visibility = View.GONE
            //총 리스트 개수 출력
            var count = supportAdapter.getCount()
            support_list_count.text = "총 $count 건"
        })

        Log.d("SupportActivity", "onCreate: ${support_rv.computeVerticalScrollOffset()}")
        /*
        if(support_rv.scrollState!=0)
            top_move.visibility = View.VISIBLE
        else
            top_move.visibility = View.GONE

         */

        //검색 버튼
        search_bar.setOnClickListener {
            startActivity(Intent(baseContext,SearchActivity::class.java))
        }
        //카테고리 버튼
        category_menu_btn.setOnClickListener {
            startActivity(Intent(baseContext,CategoryActivity::class.java))
            finish()
        }
        //top 버튼 클릭시 최상단으로 이동
        top_move_btn.setOnClickListener {
            support_rv.smoothScrollToPosition(0)
        }
        //종료버튼
        support_close_btn.setOnClickListener {
            finish()
        }

    }

    override fun onBackPressed() {
        finish()
    }

}