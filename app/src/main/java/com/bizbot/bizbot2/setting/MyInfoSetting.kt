package com.bizbot.bizbot2.setting

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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

class MyInfoSetting():AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_myinfo)

        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        var userInfo:UserModel? = null
        viewModel.getAllUser().observe(this, Observer {
            userInfo = it
        })
        val user = UserModel(0,null,null,null,null,null,null,null,null)

        //사업자 유형
        userInfo?.businessType?.let { business_type.check(it) }
        business_type.setOnCheckedChangeListener { radioGroup, i ->
            user.businessType = i
        }

        //창립일
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        establishment_layout.setOnClickListener {
            val dpd = DatePickerDialog(this, R.style.date_picker_style, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                user.establishment = "$year.${monthOfYear+1}.$dayOfMonth"
                establishment_tv.text = user.establishment
            }, year, month, day)

            dpd.show()

        }

        //대표자
        userInfo?.name?.let { ceo_name_et.setText(it) }
        user.name = ceo_name_et.text.toString()

        //성별
        userInfo?.gender?.let { gender_type.check(it) }
        gender_type.setOnCheckedChangeListener { radioGroup, i ->
            user.gender = i
        }

        //생년월일
        userInfo?.birth?.let { birth_tx.text = it }
        birth_layout.setOnClickListener {
            val dpd = DatePickerDialog(this,R.style.spinner_date_picker, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                user.birth = "$year.${monthOfYear+1}.$dayOfMonth"
                birth_tx.text = user.birth
            }, year, month, day)

            dpd.show()
        }

        //업종
        userInfo?.businessCategory?.let { category_of_business_spinner.setSelection(it) }
        ArrayAdapter.createFromResource(this,R.array.category_of_business,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                category_of_business_spinner.adapter = arrayAdapter}
        category_of_business_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                user.businessCategory = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        //사업 소재지 스피너
        userInfo?.area?.let { area_spinner.setSelection(it) }
        ArrayAdapter.createFromResource(this, R.array.area_array, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                area_spinner.adapter = arrayAdapter}
        area_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                user.area = p2
                var arrayID:Int? = null
                when(p2){
                    0->arrayID = R.array.select_default
                    1-> arrayID = R.array.Seoul
                    2-> arrayID = R.array.Busan
                    3-> arrayID = R.array.Dae_gu
                    4-> arrayID = R.array.Incheon
                    5-> arrayID = R.array.Gwangju
                    6-> arrayID = R.array.Daejeon
                    7-> arrayID = R.array.Ulsan
                    8-> arrayID = R.array.Sejong
                    9-> arrayID = R.array.Gangwon_do
                    10-> arrayID = R.array.Gyeonggi_do
                    11-> arrayID = R.array.Chung_cheong_bukdo
                    12-> arrayID = R.array.Chungcheongnam_do
                    13-> arrayID = R.array.Jeollabuk_do
                    14-> arrayID = R.array.Jeollanam_do
                    15-> arrayID = R.array.Gyeongsangbuk_do
                    16-> arrayID = R.array.Gyeongsangnam_do
                    17-> arrayID = R.array.Jeju
                }

                if(arrayID != null) {
                    userInfo?.city?.let { city_spinner.setSelection(it) }
                    ArrayAdapter.createFromResource(baseContext, arrayID, R.layout.setting_spinner_item)
                        .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                            city_spinner.adapter = arrayAdapter
                        }
                }

            }
        }

        city_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                user.city = p2
            }
        }


        //수정완료버튼
        modify_ok_btn.setOnClickListener {
            Toast.makeText(this,"btn click",Toast.LENGTH_SHORT).show()
            Log.d("MyInfoSetting", "onCreate " +
                    "${user.city}, " +
                    "${user.businessCategory}, " +
                    "${user.businessType}, " +
                    "${user.establishment}, " +
                    "${user.name}, " +
                    "${user.gender}, " +
                    "${user.birth}, " +
                    "${user.area}, " +
                    "${user.id}, ")
            //Log.d("MyInfoSetting", "onCreate: businessCategory = ${user.businessCategory}, gender = ${user.gender}, city = ${user.city}")
            viewModel.insertUser(user)
        }
        //나가기 버튼
        myinfo_close_btn.setOnClickListener { finish() }
    }
}