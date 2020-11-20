package com.bizbot.bizbot2.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.support.SupportListAdapter

class SearchAdapter(val activity: FragmentActivity,var wordList:ArrayList<String>, val editText: EditText)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var words: ArrayList<String> = wordList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.search_item_last_keyword,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        if(words.size > 8)
            return 8
        else
            return words.size
    }

    class ViewHolder(v: View):RecyclerView.ViewHolder(v) {
        val sWord = v.findViewById<TextView>(R.id.search_last_word)
        val del = v.findViewById<ImageView>(R.id.search_last_word_clear)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = words[position]
        val viewModel = ViewModelProviders.of(activity).get(AppViewModel::class.java)

        holder.sWord.text = item
        holder.sWord.setOnClickListener {
            viewModel.getSearchItem(item).observe(activity, Observer {
                editText.setText(it)
            })
        }
        holder.del.setOnClickListener {
            viewModel.delSearchItem(item)
            notifyDataSetChanged()
        }
    }

}