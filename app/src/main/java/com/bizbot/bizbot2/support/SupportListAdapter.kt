package com.bizbot.bizbot2.support

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.SEARCH_MODE
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SupportModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SupportListAdapter(var context: Context,var activity:FragmentActivity,var area: String?, var field: String?)
    : RecyclerView.Adapter<SupportListAdapter.ViewHolder>() {
    companion object{
        private val TAG = "SupportListAdapter"
    }
    lateinit var filterList : ArrayList<SupportModel>
    lateinit var sList : ArrayList<SupportModel>
    var itemSize = 20

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_support_list,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        if(filterList.size<20 || itemSize > filterList.size)
            return filterList.size
        else
            return itemSize
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = filterList[position]
        holder.apply {
            bind(items)
            bindDday(items)
            termFormat(items)
            field?.let { bindKeyword(items,context, it) }
        }

        val viewModel = ViewModelProviders.of(activity).get(AppViewModel::class.java)

        holder.newIcon.visibility = View.GONE

        //레이아웃 클릭시 디테일 페이지로 이동
        holder.layout.setOnClickListener {
            val intent = Intent(context,SupportDetail::class.java)
            intent.putExtra("detail",items)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        if(filterList[position].checkLike!!){
            holder.likeBtn.isChecked = true
            holder.likeBtn.setBackgroundResource(R.drawable.heart)
        }
        else{
            holder.likeBtn.isChecked = false
            holder.likeBtn.setBackgroundResource(R.drawable.heart_empty)
        }

        holder.likeBtn.setOnClickListener {
            if(holder.likeBtn.isChecked){
                holder.likeBtn.setBackgroundResource(R.drawable.heart)
                Toast.makeText(context,"관심사업으로 등록되었습니다.",Toast.LENGTH_SHORT).show()
                viewModel.setLike(true, filterList[position].pblancId)
            }else{
                holder.likeBtn.setBackgroundResource(R.drawable.heart_empty)
                Toast.makeText(context,"관심사업이 해제되었습니다.",Toast.LENGTH_SHORT).show()
                viewModel.setLike(false, filterList[position].pblancId)
            }
        }

        if(filterList[position].checkNew == true)
            holder.newIcon.visibility = View.VISIBLE
        else
            holder.newIcon.visibility = View.GONE


    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        private val title = v.findViewById<TextView>(R.id.title)
        private val agency = v.findViewById<TextView>(R.id.agency)
        private val term: TextView = v.findViewById(R.id.term)
        private val dDay: TextView = v.findViewById(R.id.D_day)
        val likeBtn: ToggleButton = v.findViewById(R.id.like_btn)
        val layout: ConstraintLayout = v.findViewById(R.id.support_item_layout)
        val newIcon: TextView = v.findViewById(R.id.new_icon)
        val keyWord: RecyclerView = v.findViewById(R.id.keyword_rv)

        //제목, 접수기관명 출력
        fun bind(item: SupportModel){
            title.text = item.pblancNm
            agency.text = item.jrsdInsttNm
        }

        //d-day출력
        @SuppressLint("SetTextI18n")
        fun bindDday(item: SupportModel){
            if(item.reqstBeginEndDe?.contains("~")!!) {
                dDay.visibility = View.VISIBLE
                dDay.text = "D-${calDay(item.reqstBeginEndDe)}"
            }
            else
                dDay.visibility = View.GONE
        }

        private fun calDay(term: String?):Long? {
            val word = term?.split("~")
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
            val termDate = dateFormat.parse(word?.get(1)?.substring(1, word[1].length))
            val todayDate: String = dateFormat.format(Date(System.currentTimeMillis()))
            val today: Date? = dateFormat.parse(todayDate)
            val d_dayTime: Long = termDate.time - today?.time!!
            return d_dayTime / (24 * 60 * 60 * 1000)
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

        fun bindKeyword(item:SupportModel, context: Context, field: String){
            val arr1 = item.pldirSportRealmLclasCodeNm?.split("@")
            val arr2 = item.pldirSportRealmMlsfcCodeNm?.split("@")

            val keywords = arr1?.plus(arr2!!)

            val viewManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            keyWord.layoutManager = viewManager
            keyWord.setHasFixedSize(true)
            keyWord.adapter = KeywordAdapter(context,keywords,field)
        }


    }


    /**
     * 리스트 정렬
     */
    fun sort(num:Int){
        when(num){
            1->{ //최신순 정렬
                filterList.sortByDescending { it.creatPnttm }
            }
            2->{ //제목순 정렬
                filterList.sortBy { it.pblancNm }
            }
            3->{ //이름순 정렬
                filterList.sortBy { it.jrsdInsttNm }
            }
            4->{ //접수기간 마감순 정렬
                filterList.sortBy { cutTermWords(it.reqstBeginEndDe) }
            }
        }
        notifyDataSetChanged()
    }

    /**
     * 접수기간 문자열 가공
     */
    private fun cutTermWords(term: String?):String{
        val word = term?.split("~")
        val termEnd:String?
        termEnd = if(word?.size!! > 1) //기간이 있을때
            word[1]
        else //상시 모집일때
            word[0]

        return termEnd
    }

    /**
     * 카테고리 필터
     */
    private fun categoryFilter(area:String, field: String){
        var filtering = ArrayList<SupportModel>()

        //두개다 전체인 경우
        if(area == "전체" && field == "전체")
            filtering = sList
        //지역만 선택된 경우
        else if(area != "전체" && field == "전체"){
            for(item in sList){
                if(item.pblancNm?.contains(area)!!)
                    filtering.add(item)
            }
        }
        //분야만 선택된 경우
        else if (area == "전체" && field != "전체"){
            for(item in sList){
                if(item.pldirSportRealmLclasCodeNm?.contains(field)!!)
                    filtering.add(item)
            }
        }
        //둘다 선택된 경우
        else if(area != "전체" && field != "전체"){
            for(item in sList){
                if(item.pblancNm?.contains(area)!!&&item.pldirSportRealmLclasCodeNm?.contains(field)!!)
                    filtering.add(item)
            }
        }

        filterList = filtering
        notifyDataSetChanged()

    }

    /**
     * 형태소 분석기 사용한 검색
     */
    fun posTaggingFilter(wordList:ArrayList<String>, search_mode: SEARCH_MODE):Boolean{
        val filtering = ArrayList<SupportModel>()
        for(item in sList){
            for(word in wordList){
                when(search_mode){
                    SEARCH_MODE.TITLE -> {
                        if(item.pblancNm?.toLowerCase(Locale.ROOT)?.contains(word.toLowerCase(Locale.ROOT))!!)
                            filtering.add(item)
                    }
                    SEARCH_MODE.CONTENT->{
                        if(item.bsnsSumryCn?.toLowerCase(Locale.ROOT)?.contains(word.toLowerCase(Locale.ROOT))!!)
                            filtering.add(item)
                    }
                    SEARCH_MODE.AGENCY->{
                        if(item.jrsdInsttNm?.toLowerCase(Locale.ROOT)?.contains(word.toLowerCase(Locale.ROOT))!!)
                            filtering.add(item)

                    }
                }
            }
        }

        filterList = filtering
        notifyDataSetChanged()

        return filterList.size != 0
    }

    /**
     * 리스트 갱신
     */
    fun setList(supportList: ArrayList<SupportModel>){
        this.filterList = supportList
        this.sList = supportList

        notifyDataSetChanged()

        if(area != null && field != null)
            categoryFilter(area!!, field!!)
    }

    /**
     * 리스트 개수 출력
     */
    fun getCount():Int{
        return filterList.size
    }


}