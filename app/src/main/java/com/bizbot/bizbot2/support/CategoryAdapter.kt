package com.bizbot.bizbot2.support

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel

class CategoryAdapter(var context:Context,var activity:FragmentActivity,type:Int): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val areas = arrayOf("전체","서울","부산","대구","인천","광주","대전","울산","세종","경기","강원","충북","충남","전북","전남","경북","경남","제주")
    private val field = arrayOf("전체","금융","기술","인력","수출","내수","창업","경영","제도","동반성장")
    var mCAHandler: Handler = CategoryActivity.CAHandler(Looper.myLooper()!!)
    private val categoryType = type
    private var index = 0
    lateinit var arr: Array<String>
    var check = false

    init{
        when(type){
            1-> arr = areas
            2-> arr = field
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arr[position]
        holder.apply {
            bind(item)
        }

        val viewModel = ViewModelProviders.of(activity).get(AppViewModel::class.java)
        viewModel.getArea().observe(activity, Observer {
            if(!check){
                if(changeArea(it) == item){
                    Log.d("CategoryAdapter", "onBindViewHolder: item=$item")
                    index = position
                    val message:Message = mCAHandler.obtainMessage()
                    message.what = categoryType
                    message.obj = item
                    mCAHandler.sendMessage(message)
                    notifyDataSetChanged()
                    check = true
                }
            }
        })

        //아이템 선택시
        holder.itemName.setOnClickListener {
            index = position
            //handler 로 categoryActivity 에 값 전달
            val message:Message = mCAHandler.obtainMessage()
            message.what = categoryType
            message.obj = item
            mCAHandler.sendMessage(message)
            notifyDataSetChanged()
        }
        //index == position이면 버튼 체크
        holder.itemName.isSelected = index == position

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val itemName: TextView = v.findViewById(R.id.item_btn)

        fun bind(item: String){
            itemName.text = item
        }
    }

    private fun changeArea(beforeArea:String):String{
        var afterArea = ""
        when(beforeArea){
            "서울특별시" -> afterArea = "서울"
            "부산광역시" -> afterArea = "부산"
            "대구광역시" -> afterArea = "대구"
            "인천광역시" -> afterArea = "인천"
            "광주광역시" -> afterArea = "광주"
            "대전광역시" -> afterArea = "대전"
            "울산광역시" -> afterArea = "울산"
            "세종특별자치시" -> afterArea = "세종"
            "강원도" -> afterArea = "강원"
            "경기도" -> afterArea = "경기"
            "충청북도" -> afterArea = "충북"
            "충청남도" -> afterArea = "충남"
            "전라북도" -> afterArea = "전북"
            "전라남도" -> afterArea = "전남"
            "경상남도" -> afterArea = "경남"
            "경상북도" -> afterArea = "경북"
            "제주특별자치도" -> afterArea = "제주"
        }
        return afterArea
    }


}