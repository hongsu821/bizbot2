package com.bizbot.bizbot2.support

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SupportModel
import kotlinx.android.synthetic.main.support_activity.*

class SupportActivity: AppCompatActivity() {
    private lateinit var viewAdapter: SupportListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: AppViewModel

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

        //데이터 가져오기
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        viewModel.getAllSupport().observe(this, Observer {
            viewAdapter = SupportListAdapter(baseContext,it as ArrayList<SupportModel>,areaWord,fieldWord)
            support_rv.adapter = viewAdapter

            //총 리스트 개수 출력
            var count = viewAdapter.getCount()
            support_list_count.text = "총 $count 건"
        })

        //지원사업 리사이클러뷰
        viewManager = LinearLayoutManager(this)
        support_rv.layoutManager = viewManager
        support_rv.setHasFixedSize(true)



        //카테고리 버튼
        category_menu_btn.setOnClickListener {
            startActivity(Intent(baseContext,CategoryActivity::class.java))
            finish()
        }
        //종료버튼
        support_close_btn.setOnClickListener {
            finish()
        }

    }


}