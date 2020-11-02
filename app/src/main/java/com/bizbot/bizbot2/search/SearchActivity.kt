package com.bizbot.bizbot2.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SearchWordModel
import kotlinx.android.synthetic.main.search_activity.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        //검색 버튼 클릭
        search_button.setOnClickListener {
            var inputText = search_edit_bar.text
            val searchWordModel = SearchWordModel(inputText.toString())
            viewModel.insertSearch(searchWordModel)
        }

        val viewManager = LinearLayoutManager(this)
        last_search_word.layoutManager = viewManager
        last_search_word.setHasFixedSize(true)

        viewModel.getAllSearch().observe(this, Observer {
            val searchAdapter = SearchAdapter(baseContext,this,it as ArrayList<String>,search_edit_bar)
            last_search_word.adapter = searchAdapter
        })

        //텍스트 입력창 텍스트 감지
        search_edit_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = p0.toString()
                if(input.isEmpty()){
                    search_clear.visibility = View.INVISIBLE
                }else
                    search_clear.visibility = View.VISIBLE
            }
            override fun afterTextChanged(p0: Editable?) {}
        })


        //입력한 텍스트 지우기
        search_clear.setOnClickListener {
            search_edit_bar.text = null
            search_clear.visibility = View.INVISIBLE
        }
        //모든 검색어 지우기
        all_clear.setOnClickListener {
            viewModel.delSearchAll()
        }
        //닫기 버튼 클릭시
        search_close_btn.setOnClickListener {
            finish()
        }
    }
}