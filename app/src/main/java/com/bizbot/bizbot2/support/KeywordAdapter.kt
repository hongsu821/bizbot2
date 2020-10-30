package com.bizbot.bizbot2.support

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R

class KeywordAdapter(val context:Context, val arr: List<String>?,val field: String?): RecyclerView.Adapter<KeywordAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_keyword,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        if (arr != null) {
            return arr.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = arr?.get(position)
        if (word != null) {
            holder.bind(word,field)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        private val word = v.findViewById<TextView>(R.id.item_name)
        private val layout = v.findViewById<LinearLayout>(R.id.keyword_layout)

        fun bind(item: String,field: String?){
            word.text = item
            if(field != null && field == item)
                layout.setBackgroundResource(R.drawable.item_keyword_select_style)
        }



    }

}