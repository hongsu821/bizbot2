package com.bizbot.bizbot2.setting

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.UserModel
import kotlinx.android.synthetic.main.setting_myinfo.*
import java.util.*

class MyInfoSetting:AppCompatActivity() {
    companion object{
        private val TAG = "MyInfoSetting"
    }

    private var userInfo: UserModel? = null
    var arrayID:Int = R.array.select_default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_myinfo)

        var birthDay:List<String>
        val cal = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR)
        var month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)


        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        viewModel.getAllUser().observe(this, Observer { userModel ->
            this.userInfo = userModel

            //사업자 유형 출력
            userInfo?.businessTypeNum?.let { business_type.check(it) }
            //사업체명 출력
            userInfo?.businessName?.let{ business_name_et.setText(it) }
            //대표자 출력
            userInfo?.name?.let { ceo_name_et.setText(it) }
            //성별 출력
            userInfo?.genderNum?.let { gender_type.check(it) }
            //생년월일 출력
            userInfo?.birth?.let {
                birth_tx.text = it
                if(it != ""){
                    birthDay = it.split(".")
                    year = birthDay[0].toInt()
                    month = birthDay[1].toInt()
                    day = birthDay[2].toInt()
                }
            }
            //업종 출력
            userInfo?.businessCategoryNum?.let { category_of_business_spinner.setSelection(it) }

            //알림 받을 분야
            userInfo?.fieldNum?.let { it ->
                large_spinner.setSelection(it)
                arrayID = setArray(it)
            }
            userInfo?.subclassNum?. let{city ->
                setSpinner(arrayID)
                medium_spinner.setSelection(city)
            }


        })

        //사업자 유형
        business_type.setOnCheckedChangeListener { _, i ->
            userInfo?.businessTypeNum = i
            when(i){
                R.id.corporation_business->userInfo?.businessType = corporation_business.text.toString()
                R.id.private_business->userInfo?.businessType = private_business.text.toString()
                R.id.reserve_business->userInfo?.businessType = reserve_business.text.toString()
            }
        }

        //성별
        gender_type.setOnCheckedChangeListener { _, i ->
            userInfo?.genderNum = i
            when(i){
                R.id.male -> userInfo?.gender = male.text.toString()
                R.id.female->userInfo?.gender = female.text.toString()
            }
        }

        //생년월일
        birth_layout.setOnClickListener {
            val dpd = DatePickerDialog(this,R.style.spinner_date_picker, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                userInfo?.birth = "$year.${monthOfYear+1}.$dayOfMonth"
                birth_tx.text = userInfo?.birth
            }, year, month, day)
            dpd.show()
        }

        //업종 스피너
        ArrayAdapter.createFromResource(this,R.array.category_of_business,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                category_of_business_spinner.adapter = arrayAdapter}
        category_of_business_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userInfo?.businessCategoryNum = p2
            }
        }

        //분야
        ArrayAdapter.createFromResource(this,R.array.field,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                large_spinner.adapter = arrayAdapter}
        ArrayAdapter.createFromResource(baseContext,arrayID,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                medium_spinner.adapter = arrayAdapter}
        large_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                arrayID = setArray(p2)
                userInfo?.fieldNum = p2
                setSpinner(arrayID)
            }
        }
        //소분류
        setSpinner(arrayID)
        medium_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userInfo?.subclassNum = p2
            }
        }

        //수정완료버튼
        edit_ok_btn.setOnClickListener {
            //대표자명
            userInfo?.name = ceo_name_et.text.toString()
            //사업체명
            userInfo?.businessName = business_name_et.text.toString()
            //업종
            userInfo?.businessCategory = category_of_business_spinner.selectedItem.toString()
            //알림 받을 분야
            userInfo?.field = large_spinner.selectedItem.toString()
            userInfo?.subclass = medium_spinner.selectedItem.toString()


            viewModel.insertUser(userInfo!!)
            val toast = Toast.makeText(this,"수정이 완료되었습니다.",Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER,0,0)
            toast.show()
        }
        //나가기 버튼
        myinfo_close_btn.setOnClickListener { finish() }
    }

    //두번째 스피너 설정
    fun setSpinner(arrayID:Int){
        ArrayAdapter.createFromResource(baseContext, arrayID, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                medium_spinner.adapter = arrayAdapter
            }
    }

    //두번째 스피너 array id 설정
    fun setArray(position: Int):Int{
        var arrayid = 0
        when(position){
            0->arrayid = R.array.select_default
            1->arrayid = R.array.management
            2->arrayid = R.array.finance
            3->arrayid = R.array.technique
            4->arrayid = R.array.domestic_demand
            5->arrayid = R.array.shared_growth
            6->arrayid = R.array.export
            7->arrayid = R.array.employee
            8->arrayid = R.array.system
            9->arrayid = R.array.startup
        }
        return arrayid
    }






}