package com.bizbot.bizbot2.support

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.model.SupportModel
import kotlinx.android.synthetic.main.support_details.*

class SupportDetail:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_details)

        val supportContent = intent.getParcelableExtra<SupportModel>("detail")
        val categoryKeyword = ProcessingKeyword(supportContent)

        val llLayoutManager = LinearLayoutManager(baseContext,LinearLayoutManager.HORIZONTAL,false)
        detail_keyword_rv.layoutManager = llLayoutManager
        detail_keyword_rv.setHasFixedSize(true)
        val keyAdapter = KeywordAdapter(baseContext,categoryKeyword as List<String>, null)
        detail_keyword_rv.adapter = keyAdapter

        detail_title.text = supportContent?.pblancNm
        DelHTML(supportContent?.bsnsSumryCn,detail_contents)
        DelSpecialCharacter(supportContent?.areaNm,detail_area)
        DelSpecialCharacter(supportContent?.pldirSportRealmMlsfcCodeNm,detail_field)
        detail_term.text = supportContent?.reqstBeginEndDe
        detail_agency.text = supportContent?.rceptEngnNm
        detail_department.text = supportContent?.jrsdInsttNm
        detail_tel.text = supportContent?.rceptInsttTelno
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
    fun DelHTML(str: String?, textView: TextView){
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

        val arr6 = line5.split("<div>")
        var line6 = ""
        for(word in arr6)
            line6 += "$word "

        textView.text = line6

    }

    // @ 제거
    fun DelSpecialCharacter(str: String?, textView: TextView){
        val arr = str?.split("@")
        var line = ""
        if(arr != null){
            for(word in arr)
                line += "$word, "
        }

        textView.text = line
    }

    override fun onBackPressed() {
        finish()
    }

}