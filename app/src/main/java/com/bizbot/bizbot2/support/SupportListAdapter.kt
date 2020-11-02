package com.bizbot.bizbot2.support

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SupportModel

class SupportListAdapter(var context: Context,var activity:FragmentActivity,supportList:ArrayList<SupportModel>,var area: String?, var field: String?)
    : RecyclerView.Adapter<SupportListAdapter.ViewHolder>() {
    var filteringList : ArrayList<SupportModel> = supportList
    var sList : ArrayList<SupportModel> = supportList

    init{
        if(area != null && field != null)
            CategoryFilter(area!!, field!!)
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

        holder.layout.setOnClickListener {
            val intent = Intent(context,SupportDetail::class.java)
            intent.putExtra("detail",items)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        if(filteringList[position].checkLike!!){
            holder.likeBtn.isChecked = true
            holder.likeBtn.setBackgroundResource(R.drawable.heart)
        }
        else{
            holder.likeBtn.isChecked = false
            holder.likeBtn.setBackgroundResource(R.drawable.heart_empty)
        }


        holder.likeBtn.setOnClickListener {
            val viewModel = ViewModelProviders.of(activity).get(AppViewModel::class.java)
            if(holder.likeBtn.isChecked){
                holder.likeBtn.setBackgroundResource(R.drawable.heart)
                Toast.makeText(context,"관심사업으로 등록되었습니다.",Toast.LENGTH_SHORT).show()
                viewModel.setLike(true, filteringList[position].pblancId)
            }else{
                holder.likeBtn.setBackgroundResource(R.drawable.heart_empty)
                Toast.makeText(context,"관심사업이 해제되었습니다.",Toast.LENGTH_SHORT).show()
                viewModel.setLike(false, filteringList[position].pblancId)
            }
        }

        val viewManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        holder.keyWord.layoutManager = viewManager
        holder.keyWord.setHasFixedSize(true)
        holder.keyWord.adapter = KeywordAdapter(context,SlicingWord(items),field)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        private val title = v.findViewById<TextView>(R.id.title)
        private val agency = v.findViewById<TextView>(R.id.agency)
        private val term = v.findViewById<TextView>(R.id.term)
        val likeBtn = v.findViewById<ToggleButton>(R.id.like_btn)
        val layout = v.findViewById<ConstraintLayout>(R.id.support_item_layout)
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

    /**
     * 카테고리 필터
     */
    fun CategoryFilter(area:String,field: String){
        var filtering = ArrayList<SupportModel>()

        if(area == "전체" && field == "전체")
            filtering = sList
        else if(area != "전체" && field == "전체"){
            for(item in sList){
                if(item.areaNm?.contains(area)!!)
                    filtering.add(item)
            }
        }
        else if (area == "전체" && field != "전체"){
            for(item in sList){
                if(item.pldirSportRealmLclasCodeNm?.contains(field)!!)
                    filtering.add(item)
            }
        }
        else if(area != "전체" && field != "전체"){
            for(item in sList){
                if(item.areaNm?.contains(area)!!&&item.pldirSportRealmLclasCodeNm?.contains(field)!!)
                    filtering.add(item)
            }
        }

        filteringList = filtering
        notifyDataSetChanged()

    }


    /**
     * 리스트 개수 출력
     */
    fun getCount():Int{
        return filteringList.size
    }


}