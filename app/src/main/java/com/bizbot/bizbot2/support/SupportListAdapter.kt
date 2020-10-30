package com.bizbot.bizbot2.support

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

class SupportListAdapter(var context: Context, supportList: ArrayList<SupportModel>,var area: String, var field: String): RecyclerView.Adapter<SupportListAdapter.ViewHolder>() {
    var filteringList : ArrayList<SupportModel> = supportList
    var sList : ArrayList<SupportModel> = supportList

    init{
        //CategoryFilter(area,field)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_support_list,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return filteringList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = filteringList[position]
        holder.apply {
            bind(items)
        }

        holder.newIcon.visibility = View.GONE

        val viewManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        holder.keyWord.layoutManager = viewManager
        holder.keyWord.setHasFixedSize(true)
        holder.keyWord.adapter = KeywordAdapter(context,SlicingWord(items),field)

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        private val title = v.findViewById<TextView>(R.id.title)
        private val agency = v.findViewById<TextView>(R.id.agency)
        private val term = v.findViewById<TextView>(R.id.term)
        private val likeBtn = v.findViewById<ToggleButton>(R.id.like_btn)
        val newIcon = v.findViewById<TextView>(R.id.new_icon)
        val keyWord = v.findViewById<RecyclerView>(R.id.keyword_rv)

        fun bind(item: SupportModel){
            title.text = item.pblancNm
            agency.text = item.jrsdInsttNm
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

    fun CategoryFilter(area:String,field: String){
        var filtering = ArrayList<SupportModel>()

        for(item in sList){
            if(item.areaNm?.contains(area)!! && item.areaNm?.contains(field)!!)
                filtering.add(item)
        }


        filteringList = filtering
        notifyDataSetChanged()

    }

    fun setList(list: ArrayList<SupportModel>){
        this.sList = list
        this.filteringList = list
        notifyDataSetChanged()
    }

    /**
     * 리스트 개수 출력
     */
    fun getCount():Int{
        return filteringList.size
    }


}