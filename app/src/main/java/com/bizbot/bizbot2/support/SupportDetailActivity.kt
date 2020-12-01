package com.bizbot.bizbot2.support

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.model.SupportModel
import kotlinx.android.synthetic.main.support_details.*
import java.text.SimpleDateFormat
import java.util.*

class SupportDetailActivity:AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_details)

        val supportContent = intent.getParcelableExtra<SupportModel>("detail")
        val categoryKeyword = ProcessingKeyword(supportContent)

        //키워드
        val llLayoutManager = LinearLayoutManager(baseContext,LinearLayoutManager.HORIZONTAL,false)
        detail_keyword_rv.layoutManager = llLayoutManager
        detail_keyword_rv.setHasFixedSize(true)
        val keyAdapter = KeywordAdapter(baseContext,categoryKeyword as List<String>, null)
        detail_keyword_rv.adapter = keyAdapter

        //제목
        detail_title.text = supportContent?.pblancNm
        //본문
        detail_contents.text = DelHTML(supportContent?.bsnsSumryCn)
        //지원사업 지역
        detail_area.text = DelSpecialCharacter(supportContent?.areaNm)
        //지원사업 지원분야
        detail_field.text = DelSpecialCharacter(supportContent?.pldirSportRealmMlsfcCodeNm)
        //접수기간
        detail_term.text = transTermFormat(supportContent?.reqstBeginEndDe)
        //d-day
        if(supportContent?.reqstBeginEndDe?.contains("~")!!) {
            detail_d_day.visibility = View.VISIBLE
            detail_d_day.text = "D-${printEndDay(supportContent?.reqstBeginEndDe)}"
        }
        else
            detail_d_day.visibility = View.GONE
        //접수기관
        detail_agency.text = supportContent?.rceptEngnNm
        //담당부서
        detail_department.text = supportContent?.jrsdInsttNm
        //전화번호
        detail_tel.text = supportContent?.rceptInsttTelno
        //담당자명
        detail_manager.text = supportContent?.rceptInsttChargerNm

        //홈페이지 버튼 클릭시
        detail_homepage.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(supportContent?.rceptEngnHmpgUrl)))
        }
        //닫기 버튼 클릭시
        detail_close_btn.setOnClickListener { finish() }
    }

    //키워드 단어 가공
    fun ProcessingKeyword(sModel: SupportModel?): List<String>? {
        val arr1 = sModel?.pldirSportRealmLclasCodeNm?.split("@")
        val arr2 = sModel?.pldirSportRealmMlsfcCodeNm?.split("@")

        return arr1?.plus(arr2!!)
    }

    //html 태그 제거
    fun DelHTML(str: String?):String{
        val arr1 = str?.split("<br />")
        var line1 = ""
        if (arr1 != null) {
            for(word in arr1)
                line1 += "$word \n"
        }

        val arr2 = line1.split("&nbsp;")
        var line2 = ""
        for(word in arr2)
            line2 += "$word "

        val arr3 = line2.split("<p style=\"margin: 0px\">")
        var line3 = ""
        for(word in arr3)
            line3 += "$word "

        val arr4 = line3.split("</p>")
        var line4 = ""
        for(word in arr4)
            line4 += "$word "

        line4 = line4.replace("R&amp;D", "R&D")

        val arr5 = line4.split("<div>")
        var line5 = ""
        for(word in arr5)
            line5 += "$word "

        val arr6 = line5.split("</div>")
        var line6 = ""
        for(word in arr6)
            line6 += "$word "

        return line6

    }

    //@ 제거
    fun DelSpecialCharacter(str: String?):String{
        val arr = str?.split("@")
        var line = ""
        if(arr != null){
            for(word in arr)
                line += "$word, "
        }
        return line.substring(0,line.length-2)
    }

    //접수기간 포멧 변경
    fun transTermFormat(term:String?):String{
        if(term?.contains("~")!!){
            val word = term?.split("~")
            return word?.get(0)?.substring(2,4) + "." + word?.get(0)?.substring(4,6) + "." + word?.get(0)?.substring(6, word[0].length) +"~ " +
                    word?.get(1)?.substring(3,5) + "." + word?.get(1)?.substring(5,7) + "." + word?.get(1)?.substring(7, word[1].length)
        }
        else
            return term
    }

    fun printEndDay(term: String?):Long{
        val word = term?.split("~")
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val termDate = dateFormat.parse(word?.get(1)?.substring(1, word[1].length))
        val todayDate: String = dateFormat.format(Date(System.currentTimeMillis()))
        val today: Date? = dateFormat.parse(todayDate)
        val d_dayTime: Long = termDate.time - today?.time!!
        return d_dayTime / (24 * 60 * 60 * 1000)
    }

    override fun onBackPressed() {
        finish()
    }

}