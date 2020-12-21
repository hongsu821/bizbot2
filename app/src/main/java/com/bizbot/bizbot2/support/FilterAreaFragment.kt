package com.bizbot.bizbot2.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bizbot.bizbot2.R
import kotlinx.android.synthetic.main.support_filter_arear_fragment.*

class FilterAreaFragment:Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.support_filter_arear_fragment,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //지역 리사이클러뷰
        val viewManager1 = GridLayoutManager(activity,2)
        area_category_rv.layoutManager = viewManager1
        area_category_rv.setHasFixedSize(true)
        val areaAdapter = FilterAdapter(view.context,1)
        area_category_rv.adapter = areaAdapter
    }


}