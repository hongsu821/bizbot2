package com.bizbot.bizbot2.support

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.SupportModel
import kotlinx.android.synthetic.main.favourite_activity.*

class FavouriteActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favourite_activity)

        val viewManager = LinearLayoutManager(this)
        favourite_rv.layoutManager = viewManager
        favourite_rv.setHasFixedSize(true)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        viewModel.getLikeList().observe(this, Observer {
            val favouriteAdapter = SupportListAdapter(baseContext,this,it as ArrayList<SupportModel>,null,null)
            favourite_rv.adapter = favouriteAdapter
        })



        favourite_close_btn.setOnClickListener {
            finish()
        }
    }
}