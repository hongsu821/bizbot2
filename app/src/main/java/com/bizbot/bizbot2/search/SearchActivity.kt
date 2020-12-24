package com.bizbot.bizbot2.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.SEARCH_MODE
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.support.SupportListAdapter
import kotlinx.android.synthetic.main.search_activity.*
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran
import java.io.*

class SearchActivity : AppCompatActivity() {
    lateinit var resultWords:ArrayList<String>

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)


        //검색 결과 리사이클러뷰
        search_result_rv.layoutManager = LinearLayoutManager(this)
        search_result_rv.isNestedScrollingEnabled = false
        search_result_rv.setHasFixedSize(true)
        val searchResultAdapter = SupportListAdapter(baseContext,this)
        searchResultAdapter.area = "전체"
        searchResultAdapter.field = "전체"
        viewModel.getAllSupport().observe(this, Observer {
            searchResultAdapter.setList(it as ArrayList<SupportModel>)
            search_result_rv.adapter = searchResultAdapter
        })


        //검색어 리사이클러뷰
        val viewManager = LinearLayoutManager(this)
        viewManager.reverseLayout = true
        viewManager.stackFromEnd = true
        search_word_rv.layoutManager = viewManager
        search_word_rv.isNestedScrollingEnabled = false
        search_word_rv.setHasFixedSize(true)
        viewModel.getAllSearch().observe(this, Observer {
            val searchAdapter = SearchAdapter(this,it as ArrayList<String>,search_edit_bar)
            search_word_rv.adapter = searchAdapter

        })

        //텍스트 입력창 텍스트 감지
        search_edit_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = p0.toString()
                if(input.isEmpty()) {
                    search_clear.visibility = View.GONE
                    search_result_layout.visibility = View.GONE
                }
                else
                    search_clear.visibility = View.VISIBLE
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        //검색 버튼 클릭
        search_button.setOnClickListener {
            //edit text 에서 검색어 받아오기
            val inStr = search_edit_bar.text.toString()
            val word = SearchWordModel(inStr)
            viewModel.insertSearch(word)
            //형태소 분석기
            resultWords = posTagging(inStr)
            //검색어 레이아웃 숨기기
            search_word_layout.visibility = View.GONE
            //검색결과 레이아웃 출력
            search_result_layout.visibility = View.VISIBLE
            //제목 검색 결과 default
            search_title_btn.setBackgroundResource(R.drawable.button_on)
            printSearchResult(SEARCH_MODE.TITLE,searchResultAdapter,resultWords)
        }

        //제목 검색 버튼
        search_title_btn.setOnClickListener {
            search_title_btn.setBackgroundResource(R.drawable.button_on)
            search_content_btn.background = null
            search_agency_btn.background = null
            printSearchResult(SEARCH_MODE.TITLE,searchResultAdapter,resultWords)
        }
        //내용 검색 버튼
        search_content_btn.setOnClickListener {
            search_title_btn.background = null
            search_content_btn.setBackgroundResource(R.drawable.button_on)
            search_agency_btn.background = null
            printSearchResult(SEARCH_MODE.CONTENT,searchResultAdapter,resultWords)
        }
        //기관 검색 버튼
        search_agency_btn.setOnClickListener {
            search_title_btn.background = null
            search_content_btn.background = null
            search_agency_btn.setBackgroundResource(R.drawable.button_on)
            printSearchResult(SEARCH_MODE.AGENCY,searchResultAdapter,resultWords)
        }

        //입력한 텍스트 지우기
        search_clear.setOnClickListener {
            search_edit_bar.text = null
            search_clear.visibility = View.INVISIBLE
            search_result_layout.visibility = View.GONE
            search_word_layout.visibility = View.VISIBLE
        }
        //스크롤뷰 위치 감지
        search_result_scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
            if (search_result_scrollView.computeVerticalScrollOffset() == 0)
                search_top_btn.visibility = View.GONE
            else
                search_top_btn.visibility = View.VISIBLE

        }
        //top 버튼 클릭시 최상단으로 이동
        search_top_btn.setOnClickListener {
            search_result_scrollView.smoothScrollTo(0, 0)
        }
        //더보기 버튼 클릭시
        search_add_btn.setOnClickListener {
            //클릭할때 마다 15개씩 보여주기
            searchResultAdapter.itemSize += 15
            //모든 게시글 출력시 더보기 버튼 지우기
            if (searchResultAdapter.itemSize > searchResultAdapter.getCount())
                search_add_btn.visibility = View.GONE
            searchResultAdapter.notifyDataSetChanged()
        }

        //모든 검색어 지우기
        search_word_clear_all.setOnClickListener { viewModel.delSearchAll() }
        //닫기 버튼 클릭시
        search_close_btn.setOnClickListener { finish() }

    }

    //검색 결과
    private fun printSearchResult(searchMode: SEARCH_MODE, adapter:SupportListAdapter, wordList:ArrayList<String>){
        val resultCheck = adapter.posTaggingFilter(wordList,searchMode)
        if(resultCheck){
            search_result_null.visibility = View.GONE
            search_result_rv.visibility = View.VISIBLE
        }else{
            search_result_null.visibility = View.VISIBLE
            search_result_rv.visibility = View.GONE
        }
        //더보기 버튼 지우기
        if (adapter.itemSize > adapter.getCount())
            search_add_btn.visibility = View.GONE
        //더보기 버튼 보이기
        else search_add_btn.visibility = View.VISIBLE
        //스크롤 최상단 이동
        search_result_scrollView.smoothScrollTo(0,0)
        //검색 결과 개수 출력
        search_result_count.text = adapter.getCount().toString()
    }

    //형태소 분석기
    private fun posTagging(inputStr:String):ArrayList<String>{
        val result:ArrayList<String> = ArrayList()

        val path = "$filesDir/user.txt"
        val file = File(path)
        initFile(file)

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

        return processingWord(result)

    }

    //raw 에서 word set 불러오기
    private fun initFile(file : File){
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
    private fun processingWord(wordList: ArrayList<String>):ArrayList<String>{
        var processedWord = ArrayList<String>()
        for(word in wordList){

            var pWord = word.replace(" ","")//공백 제거

            if(pWord == "지원사업"|| pWord == "지원" || pWord == "사업")
                continue

            processedWord.add(pWord)

            if(pWord == "코트라")
                processedWord.add("kotra")

            if(pWord == "알앤디"){
                processedWord.add("r&d")
                processedWord.add("R&amp;D")
            }
            if(pWord == "r&d")
                processedWord.add("R&amp;D")
            if(pWord == "R&D")
                processedWord.add("R&amp;D")

            if(pWord == "알앤비디"){
                processedWord.add("R&amp;BD")
                processedWord.add("r&bd")
            }
            if(pWord == "r&bd")
                processedWord.add("R&amp;BD")
            if(pWord == "R&BD")
                processedWord.add("R&amp;BD")
        }

        return processedWord
    }

    override fun onBackPressed() {
        finish()
    }
}