package com.bizbot.bizbot2.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.support.SupportListAdapter
import kotlinx.android.synthetic.main.search_activity.*
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran
import java.io.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        //검색 결과 리사이클러뷰
        val viewManager2 = LinearLayoutManager(this)
        search_result_rv.setHasFixedSize(true)
        search_result_rv.layoutManager = viewManager2
        val searchResultAdapter = SupportListAdapter(baseContext,this,null,null)
        viewModel.getAllSupport().observe(this, Observer {
            searchResultAdapter.setList(it as ArrayList<SupportModel>)
            search_result_rv.adapter = searchResultAdapter
        })


        //검색어 리사이클러뷰
        val viewManager = LinearLayoutManager(this)
        viewManager.reverseLayout = true
        viewManager.stackFromEnd = true
        last_search_word.layoutManager = viewManager
        last_search_word.setHasFixedSize(true)
        viewModel.getAllSearch().observe(this, Observer {
            val searchAdapter = SearchAdapter(this,it as ArrayList<String>,search_edit_bar)
            last_search_word.adapter = searchAdapter
        })

        //텍스트 입력창 텍스트 감지
        search_edit_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = p0.toString()
                if(input.length < 1) {
                    search_clear.visibility = View.INVISIBLE
                    search_result_layout.visibility = View.GONE
                }
                else
                    search_clear.visibility = View.VISIBLE


            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        //검색 버튼 클릭
        search_button.setOnClickListener {
            var inputText = search_edit_bar.text
            val searchWordModel = SearchWordModel(inputText.toString())
            viewModel.insertSearch(searchWordModel)
            if(searchResultAdapter.getFilter(inputText.toString()))
                search_result_layout.visibility = View.VISIBLE
            else
                search_result_layout.visibility = View.GONE
            search_result_null.visibility = View.GONE
        }
        //입력한 텍스트 지우기
        search_clear.setOnClickListener {
            search_edit_bar.text = null
            search_clear.visibility = View.INVISIBLE
            search_result_layout.visibility = View.GONE
        }
        //모든 검색어 지우기
        all_clear.setOnClickListener {
            viewModel.delSearchAll()
        }
        //닫기 버튼 클릭시
        search_close_btn.setOnClickListener {
            finish()
        }

        test.setOnClickListener {
            val inStr = search_edit_bar.text.toString()

            val resultWords = PosTagging(inStr)
            search_result_layout.visibility = View.VISIBLE
            var line = ""
            for(word in resultWords)
                line += "$word, "
            line.substring(0,line.length-2)
            search_analysis_word.text = line




        }
    }

    fun PosTagging(inputStr:String):ArrayList<String>{
        var result:ArrayList<String> = ArrayList()

        val path = "$filesDir/user.txt"
        val file = File(path)
        FileInit(file)

        val start = System.currentTimeMillis()
        val komoran = Komoran(DEFAULT_MODEL.LIGHT)
        komoran.setUserDic(path)
        val tokens = komoran.analyze(inputStr).tokenList
        for(token in tokens){
            if(token.pos.equals("NNG")||token.pos.equals("NNP")||token.pos.equals("SL"))
                result.add(token.morph)
        }
        val end = System.currentTimeMillis()
        println("time = ${(end-start)/1000}")

        return result

    }

    //raw 에서 word set 불러오기
    fun FileInit(file : File){
        try{
            val inputStream = resources.openRawResource(R.raw.user)
            val reader = InputStreamReader(inputStream,"utf-8")
            val bufferReader = BufferedReader(reader)
            val fw = FileWriter(file)
            var line:String? = ""
            var str = ""
            while(line != null){
                line = bufferReader.readLine()
                str += "$line\n"
            }
            fw.write(str)
            fw.close()
            bufferReader.close()
        }catch (e: IOException){ e.printStackTrace()}


    }

    //문자열 가공
    fun ProcessingWord(){

    }

    override fun onBackPressed() {
        finish()
    }
}