package com.bizbot.bizbot2.support

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R

class SupportFilterAdapter(var context:Context, type:Int): RecyclerView.Adapter<SupportFilterAdapter.ViewHolder>() {
    private val areas = arrayOf("전체","서울","부산","대구","인천","광주","대전","울산","세종","경기","강원","충북","충남","전북","전남","경북","경남","제주")
    private val field = arrayOf("전체","금융","기술","인력","수출","내수","창업","경영","제도","동반성장")
    var mCAHandler: Handler = SupportFilterActivity.FilterHandler(Looper.myLooper()!!)
    private val filterType = type
    private var index = 0
    lateinit var arr: Array<String>
    var selItem = ""

    init{
        when(type){
            1-> arr = areas
            2-> arr = field
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(context).inflate(R.layout.item_filter,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arr[position]

        holder.itemName.text = arr[position]

        //아이템 선택시
        holder.itemName.setOnClickListener {
            index = position
            //handler 로 categoryActivity 에 값 전달
            val message:Message = mCAHandler.obtainMessage()
            message.what = filterType
            message.obj = item
            mCAHandler.sendMessage(message)
            selItem = item
            notifyDataSetChanged()
        }

        //index == position 이면 버튼 체크
        if(index == position){
            holder.itemName.setTextColor(Color.parseColor("#FFB449"))
            holder.itemImage.setImageResource(R.drawable.check_select)
        }else{
            holder.itemName.setTextColor(Color.parseColor("#000000"))
            holder.itemImage.setImageResource(R.drawable.check_default)
        }


    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val itemName: TextView = v.findViewById(R.id.item_btn)
        val itemImage: ImageView = v.findViewById(R.id.item_image)
    }

}