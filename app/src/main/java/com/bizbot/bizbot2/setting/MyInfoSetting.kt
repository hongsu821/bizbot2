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

class MyInfoSetting:AppCompatActivity() {
    companion object{
        private val TAG = "MyInfoSetting"
    }

    private var userInfo: UserModel? = null
    var arrayID:Int = R.array.select_default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_myinfo)

        var foundingDay:List<String>
        var birthDay:List<String>
        val cal = Calendar.getInstance()
        var year1 = cal.get(Calendar.YEAR)
        var month1 = cal.get(Calendar.MONTH)
        var day1 = cal.get(Calendar.DAY_OF_MONTH)
        var year2 = cal.get(Calendar.YEAR)
        var month2 = cal.get(Calendar.MONTH)
        var day2 = cal.get(Calendar.DAY_OF_MONTH)


        val viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        viewModel.getAllUser().observe(this, Observer { userModel ->
            this.userInfo = userModel

            //Log.d(TAG, "observe userInfo = ${userInfo?.id}, ${userInfo?.businessType}, ${userInfo?.businessName}, ${userInfo?.establishment}, " +
            //        "${userInfo?.name}, ${userInfo?.gender}, ${userInfo?.birth}, ${userInfo?.businessCategory}, ${userInfo?.city}, ${userInfo?.area}")

            //사업자 유형 출력
            userInfo?.businessType?.let {
                when(userInfo?.businessType){
                    R.id.reserve_business-> reserve_business.isChecked = true
                    R.id.private_business-> private_business.isChecked = true
                    R.id.corporation_business->corporation_business.isChecked = true
                }
            }
            //사업체명 출력
            userInfo?.businessName?.let{ business_name_et.setText(it) }
            //창립일 출력
            userInfo?.establishment?.let{
                establishment_tv.text = it
                if(it != "") {
                    foundingDay = it.split(".")
                    year1 = foundingDay[0].toInt()
                    month1 = foundingDay[1].toInt()
                    day1 = foundingDay[2].toInt()
                }
            }
            //대표자 출력
            userInfo?.name?.let { ceo_name_et.setText(it) }
            //성별 출력
            userInfo?.gender?.let { gender_type.check(it) }
            //생년월일 출력
            userInfo?.birth?.let {
                birth_tx.text = it
                if(it != ""){
                    birthDay = it.split(".")
                    year2 = birthDay[0].toInt()
                    month2 = birthDay[1].toInt()
                    day2 = birthDay[2].toInt()
                }
            }
            //업종 출력
            userInfo?.businessCategory?.let { category_of_business_spinner.setSelection(it) }
            //사업 소재지 출력
            userInfo?.area?.let {
                area_spinner.setSelection(it)
                arrayID = setArray(it)
                setSpinner(arrayID)
            }
            userInfo?.city?.let {
                Log.d(TAG, "onCreate: city=${userInfo?.city}")
                city_spinner.setSelection(it)
            }

        })


        test.setOnClickListener {
            Log.d(TAG, "onCreate userInfo = ${userInfo?.id}, ${userInfo?.businessType}, ${userInfo?.businessName}, ${userInfo?.establishment}, " +
                    "${userInfo?.name}, ${userInfo?.gender}, ${userInfo?.birth}, ${userInfo?.businessCategory}, ${userInfo?.area}, ${userInfo?.city}")
        }

        //사업자 유형
        business_type.setOnCheckedChangeListener { radioGroup, i ->
            userInfo?.businessType = i
        }

        //창립일
        establishment_layout.setOnClickListener {
            val dpd = DatePickerDialog(this, R.style.spinner_date_picker,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    userInfo?.establishment = "$year.${monthOfYear + 1}.$dayOfMonth"
                    establishment_tv.text = userInfo?.establishment
                }, year1, month1, day1
            )
            dpd.show()
        }

        //성별
        gender_type.setOnCheckedChangeListener { radioGroup, i ->
            userInfo?.gender = i
        }

        //생년월일
        birth_layout.setOnClickListener {
            val dpd = DatePickerDialog(this,R.style.spinner_date_picker, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                userInfo?.birth = "$year.${monthOfYear+1}.$dayOfMonth"
                birth_tx.text = userInfo?.birth
            }, year2, month2, day2)
            dpd.show()
        }

        //업종 스피너
        ArrayAdapter.createFromResource(this,R.array.category_of_business,R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                category_of_business_spinner.adapter = arrayAdapter}
        category_of_business_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userInfo?.businessCategory = p2
            }
        }

        //지역 스피너
        ArrayAdapter.createFromResource(this, R.array.area_array, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                area_spinner.adapter = arrayAdapter}
        ArrayAdapter.createFromResource(baseContext, arrayID, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                city_spinner.adapter = arrayAdapter
            }
        area_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userInfo?.area = p2
                arrayID = setArray(p2)
                setSpinner(arrayID)
            }
        }
        city_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userInfo?.city = p2
            }
        }

        //수정완료버튼
        edit_ok_btn.setOnClickListener {
            userInfo?.name = ceo_name_et.text.toString()
            userInfo?.businessName = business_name_et.text.toString()
            viewModel.insertUser(userInfo!!)
            Toast.makeText(this,"수정이 완료되었습니다.",Toast.LENGTH_SHORT).show()
        }
        //나가기 버튼
        myinfo_close_btn.setOnClickListener { finish() }
    }

    fun setSpinner(arrayID:Int){
        ArrayAdapter.createFromResource(baseContext, arrayID, R.layout.setting_spinner_item)
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                city_spinner.adapter = arrayAdapter
            }
    }

    fun setArray(position : Int):Int{
        var arrayId = 0
        when(position){
            0-> arrayId = R.array.select_default
            1-> arrayId = R.array.Seoul
            2-> arrayId = R.array.Busan
            3-> arrayId = R.array.Dae_gu
            4-> arrayId = R.array.Incheon
            5-> arrayId = R.array.Gwangju
            6-> arrayId = R.array.Daejeon
            7-> arrayId = R.array.Ulsan
            8-> arrayId = R.array.Sejong
            9-> arrayId = R.array.Gangwon_do
            10-> arrayId = R.array.Gyeonggi_do
            11-> arrayId = R.array.Chung_cheong_bukdo
            12-> arrayId = R.array.Chungcheongnam_do
            13-> arrayId = R.array.Jeollabuk_do
            14-> arrayId = R.array.Jeollanam_do
            15-> arrayId = R.array.Gyeongsangbuk_do
            16-> arrayId = R.array.Gyeongsangnam_do
            17-> arrayId = R.array.Jeju
        }
        return arrayId
    }




}