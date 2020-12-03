package com.bizbot.bizbot2.support

import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.search.SearchActivity
import kotlinx.android.synthetic.main.support_activity.*

class SupportActivity: AppCompatActivity() {
    var supportList:ArrayList<SupportModel>? = null

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_activity)
        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        //선택한 분야 가져오기
        var fieldWord = intent.getStringExtra("field")
        if(fieldWord == null || fieldWord == "")
            fieldWord = "전체"
        //선택한 지역 출력
        var areaWord = intent.getStringExtra("area")
        viewModel.getArea().observe(this, Observer {
            if(it == "선택")
                areaWord = "전체"
            areaWord = changeArea(it)
            area_state.text = changeArea(it)
        })
        if(areaWord == null || areaWord == "")
            areaWord = "전체"
        area_state.text = areaWord

        //지원사업 리사이클러뷰
        val viewManager = LinearLayoutManager(this)
        support_rv.layoutManager = viewManager
        support_rv.isNestedScrollingEnabled = false
        support_rv.setHasFixedSize(true)
        val supportAdapter = SupportListAdapter(baseContext,this,areaWord,fieldWord)

        //데이터 가져오기
        viewModel.getAllSupport().observe(this, Observer {
            supportList = it as ArrayList<SupportModel>
            if(supportList != null){
                supportAdapter.setList(supportList!!)
                support_rv.adapter = supportAdapter
                //로딩바 지우기
                progressBar.visibility = View.GONE
                //더보기 버튼 보이기
                support_add_btn.visibility = View.VISIBLE
                //총 리스트 개수 출력
                val count = supportAdapter.getCount()
                support_list_count.text = "총 $count 건"
                if(supportAdapter.itemSize > supportAdapter.getCount())
                    support_add_btn.visibility = View.GONE
            }

        })

        //spinner
        ArrayAdapter.createFromResource(this,R.array.sort_mode,R.layout.spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                support_spinner.adapter = arrayAdapter}
        support_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(supportList != null){
                    supportAdapter.sort(p2)
                    support_scroll_view.smoothScrollTo(0,0)
                }
            }
        }

        //리사이클러뷰 스크롤 위치 감지
        support_scroll_view.setOnScrollChangeListener{ _, _, _, _, _ ->
            if(support_scroll_view.computeVerticalScrollOffset() == 0)
                top_move_btn.visibility = View.GONE
            else
                top_move_btn.visibility = View.VISIBLE

        }

        //top 버튼 클릭시 최상단으로 이동
        top_move_btn.setOnClickListener {
            support_scroll_view.smoothScrollTo(0,0)
        }
        //더보기 버튼 클릭시
        support_add_btn.setOnClickListener {
            supportAdapter.itemSize += 20
            if(supportAdapter.itemSize > supportAdapter.getCount())
                support_add_btn.visibility = View.GONE
            supportAdapter.notifyDataSetChanged()
        }
        //검색 버튼
        search_bar.setOnClickListener {
            startActivity(Intent(baseContext,SearchActivity::class.java))
        }
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

    private fun changeArea(beforeArea:String):String{
        var afterArea = ""
        when(beforeArea){
            "서울특별시" -> afterArea = "서울"
            "부산광역시" -> afterArea = "부산"
            "대구광역시" -> afterArea = "대구"
            "인천광역시" -> afterArea = "인천"
            "광주광역시" -> afterArea = "광주"
            "대전광역시" -> afterArea = "대전"
            "울산광역시" -> afterArea = "울산"
            "세종특별자치시" -> afterArea = "세종"
            "강원도" -> afterArea = "강원"
            "경기도" -> afterArea = "경기"
            "충청북도" -> afterArea = "충북"
            "충청남도" -> afterArea = "충남"
            "전라북도" -> afterArea = "전북"
            "전라남도" -> afterArea = "전남"
            "경상남도" -> afterArea = "경남"
            "경상북도" -> afterArea = "경북"
            "제주특별자치도" -> afterArea = "제주"
        }
        return afterArea
    }

    override fun onBackPressed() {
        finish()
    }

}