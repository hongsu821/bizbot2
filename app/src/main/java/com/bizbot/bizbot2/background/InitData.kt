package com.bizbot.bizbot2.background

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.bizbot.bizbot2.room.AppDatabase
import com.bizbot.bizbot2.room.model.SupportModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class InitData(var context: Context) {
    private val TAG = "SynchronizationData"

    fun init(): Int{
        val supportURL = "http://www.bizinfo.go.kr/uss/rss/bizPersonaRss.do?dataType=json"

        try{
            val start = System.currentTimeMillis()

            //서버 연결
            val requestURLConnection = RequestURLConnection(supportURL)
            val line = requestURLConnection.DataLoad()

            /*디버깅용
            val assetManager = context.assets
            val inputStream= assetManager.open("data.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
             */

            val json:JSONObject? = JSONObject(line)
            val jsonArray: JSONArray = json?.getJSONArray("jsonArray")!!

            val db: AppDatabase = Room.databaseBuilder(context,AppDatabase::class.java,"app_db").build()

            for(i in 0 until jsonArray.length()){
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val supportItem: SupportModel = JsonParsing_support(jsonObject)
                db.supportDAO().insert(supportItem)
            }

            val end = System.currentTimeMillis()

            Log.d(TAG, "data loading: "+(end-start)/1000+" s")

            db.close()

            return 1

        }catch (e: JSONException){
            e.printStackTrace()
            return 0
        }catch (e: ParseException){
            e.printStackTrace()
            return 0
        }
    }

    fun JsonParsing_support(jsonObject: JSONObject): SupportModel{
        val s_list = SupportModel(
            jsonObject.getString("pblancId"),
            jsonObject.getString("industNm"),
            jsonObject.getString("rceptInsttEmailAdres"),
            jsonObject.getInt("inqireCo"),
            jsonObject.getString("rceptEngnHmpgUrl"),
            jsonObject.getString("pblancUrl"),
            jsonObject.getString("jrsdInsttNm"),
            jsonObject.getString("rceptEngnNm"),
            jsonObject.getString("entrprsStle"),
            jsonObject.getString("pldirSportRealmLclasCodeNm"),
            jsonObject.getString("trgetNm"),
            jsonObject.getString("rceptInsttTelno"),
            jsonObject.getString("bsnsSumryCn"),
            jsonObject.getString("reqstBeginEndDe"),
            jsonObject.getString("areaNm"),
            jsonObject.getString("pldirSportRealmMlsfcCodeNm"),
            jsonObject.getString("rceptInsttChargerDeptNm"),
            jsonObject.getString("rceptInsttChargerNm"),
            jsonObject.getString("pblancNm"),
            jsonObject.getString("creatPnttm"),
            false,false
        )

        return s_list

    }
}