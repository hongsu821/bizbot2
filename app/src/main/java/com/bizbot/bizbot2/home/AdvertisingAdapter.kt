package com.bizbot.bizbot2.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.model.SupportModel

class AdvertisingAdapter(val context: Context,private var adList: ArrayList<SupportModel>) : RecyclerView.Adapter<AdvertisingAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return 2
    }

    //아이템 레이아웃
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertisingAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_support_list,parent,false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        private val title = v.findViewById<TextView>(R.id.title)
        private val agency = v.findViewById<TextView>(R.id.agency)
        private val term = v.findViewById<TextView>(R.id.term)
        private val likeBtn = v.findViewById<ToggleButton>(R.id.like_btn)
        private val keyWord = v.findViewById<RecyclerView>(R.id.keyword_rv)

        fun bind(item: SupportModel){
            title.text = item.pblancNm
            agency.text = item.jrsdInsttNm
            term.text = item.reqstBeginEndDe
            likeBtn.visibility = View.INVISIBLE

        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = adList[position]
        holder.apply {
            bind(items)
        }
    }


}