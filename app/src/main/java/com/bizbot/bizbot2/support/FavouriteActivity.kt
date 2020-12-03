package com.bizbot.bizbot2.support

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SupportModel
import kotlinx.android.synthetic.main.support_favourite.*
import kotlinx.android.synthetic.main.support_favourite.top_move_btn

class FavouriteActivity : AppCompatActivity(){
    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_favourite)

        val viewManager = LinearLayoutManager(this)
        favourite_rv.layoutManager = viewManager
        favourite_rv.setHasFixedSize(true)
        val favouriteAdapter = SupportListAdapter(baseContext,this)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        viewModel.getLikeList().observe(this, Observer {
            favouriteAdapter.setList(it as ArrayList<SupportModel>)
            favourite_rv.adapter = favouriteAdapter
        })

        //리사이클러뷰 스크롤 위치 감지
        favourite_rv.setOnScrollChangeListener{ _, _, _, _, _ ->
            if(favourite_rv.computeVerticalScrollOffset() == 0)
                top_move_btn.visibility = View.GONE
            else
                top_move_btn.visibility = View.VISIBLE
        }

        //top 버튼 클릭시 최상단으로 이동
        top_move_btn.setOnClickListener {
            favourite_rv.smoothScrollToPosition(0)
        }
        //뒤로가기 버튼
        favourite_close_btn.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}