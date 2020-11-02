package com.bizbot.bizbot2.support

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R
import kotlinx.android.synthetic.main.category_activity.*
import kotlinx.android.synthetic.main.support_activity.*

class CategoryActivity: AppCompatActivity() {
    var areaItem = ""
    var fieldItem = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_activity)

        val intent = Intent(this,SupportActivity::class.java)

        //지역 리사이클러뷰
        val viewManager1 = GridLayoutManager(this,5)
        area_category_rv.layoutManager = viewManager1
        area_category_rv.setHasFixedSize(true)
        val areaAdapter = CategoryAdapter(baseContext,1)
        area_category_rv.adapter = areaAdapter
        areaAdapter.mCAHandler = object: Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                areaItem = msg.obj as String
                intent.putExtra("area",areaItem)
                //Log.d("CategoryActivity", "onCreate: area=$areaItem")
            }
        }

        //분야 리사이클러뷰
        val viewManager2 = GridLayoutManager(this,3)
        field_category_rv.layoutManager = viewManager2
        field_category_rv.setHasFixedSize(true)
        val fieldAdapter = CategoryAdapter(baseContext,2)
        field_category_rv.adapter = fieldAdapter
        fieldAdapter.mCAHandler = object: Handler(Looper.myLooper()!!){
            override fun handleMessage(msg: Message) {
                fieldItem = msg.obj as String
                intent.putExtra("field",fieldItem)
                //Log.d("CategoryActivity", "onCreate: field=$fieldItem")
            }
        }

        //적용하기 버튼
        category_ok_btn.setOnClickListener {
            startActivity(intent)
            finish()
        }
        //나가기 버튼
        category_close_btn.setOnClickListener {
            startActivity(Intent(this,SupportActivity::class.java))
            finish()
        }

    }


    class CAHandler(looper: Looper):Handler(looper){
        override fun handleMessage(msg: Message) {
        }
    }


}