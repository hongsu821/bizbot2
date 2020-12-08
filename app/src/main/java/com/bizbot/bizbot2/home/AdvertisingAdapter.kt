package com.bizbot.bizbot2.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.support.KeywordAdapter
import com.bizbot.bizbot2.support.SupportListAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdvertisingAdapter(val context: Context,private var adList: ArrayList<SupportModel>) : RecyclerView.Adapter<AdvertisingAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return 2
    }

    //아이템 레이아웃
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertisingAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_support,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = adList[position]
        holder.apply {
            bind(items)
            printDDay(items)
            termFormat(items)
        }
        //키워드 리사이클러뷰
        val viewManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        holder.keyWord.layoutManager = viewManager
        holder.keyWord.setHasFixedSize(true)
        holder.keyWord.adapter = KeywordAdapter(context,SlicingWord(items),"전체")
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private val title = v.findViewById<TextView>(R.id.title)
        private val agency = v.findViewById<TextView>(R.id.agency)
        private val likeBtn = v.findViewById<ToggleButton>(R.id.like_btn)
        private val term:TextView = v.findViewById(R.id.term)
        val dDay:TextView = v.findViewById(R.id.D_day)
        val keyWord:RecyclerView = v.findViewById(R.id.keyword_rv)

        fun bind(item: SupportModel){
            title.text = item.pblancNm
            agency.text = item.jrsdInsttNm
            likeBtn.visibility = View.INVISIBLE
        }

        //d-day 출력
        @SuppressLint("SetTextI18n")
        fun printDDay(item: SupportModel){
            if(item.reqstBeginEndDe?.contains("~")!!){
                val word = item.reqstBeginEndDe?.split("~")
                val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
                val termDate = dateFormat.parse(word?.get(1)?.substring(1, word[1].length))
                val today = Date(System.currentTimeMillis())
                val dDayTime: Long = termDate.time - today.time
                val d_day: Long = dDayTime/(24*60*60*1000)
                dDay.text = ("D-${d_day}")
            }
            else
                dDay.visibility = View.GONE
        }

        //접수기간 출력
        @SuppressLint("SetTextI18n")
        fun termFormat(item: SupportModel){
            if(item.reqstBeginEndDe?.contains("~")!!){
                val word = item.reqstBeginEndDe?.split("~")
                term.text =  word?.get(0)?.substring(2,4) + "." + word?.get(0)?.substring(4,6) + "." + word?.get(0)?.substring(6, word[0].length) +"~ " +
                        word?.get(1)?.substring(3,5) + "." + word?.get(1)?.substring(5,7) + "." + word?.get(1)?.substring(7, word[1].length)
            }
            else
                term.text = item.reqstBeginEndDe
        }

    }

    /**
     * 키워드에 쓰일 단어 자르는 함수
     */
    fun SlicingWord(str: SupportModel): List<String>? {
        val arr1 = str.pldirSportRealmLclasCodeNm?.split("@")
        val arr2 = str.pldirSportRealmMlsfcCodeNm?.split("@")
        val wordList = arr1?.plus(arr2!!)

        return wordList
    }


}