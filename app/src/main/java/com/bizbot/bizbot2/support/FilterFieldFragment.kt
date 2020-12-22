package com.bizbot.bizbot2.support

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bizbot.bizbot2.R
import kotlinx.android.synthetic.main.support_filter_fragment.*

class FilterFieldFragment: Fragment() {
    var fieldItem = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.support_filter_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //분야 리사이클러뷰
        val viewManager1 = GridLayoutManager(activity,1)
        filter_rv.layoutManager = viewManager1
        filter_rv.setHasFixedSize(true)
        val areaAdapter = SupportFilterAdapter(view.context,2)
        filter_rv.adapter = areaAdapter
        areaAdapter.mCAHandler = Handler(Looper.myLooper()!!){
            arguments = Bundle().apply {
                putString("field",it.obj.toString())
            }

            true
        }
    }
}