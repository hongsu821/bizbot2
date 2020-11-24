package com.bizbot.bizbot2.background

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.room.AppDatabase
import com.bizbot.bizbot2.room.AppViewModel
import com.bizbot.bizbot2.room.model.PermitModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.support.SupportActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SynchronizationData(var context: Context) {
    companion object{
        private val TAG = "SynchronizationData"
        val CHANNEL_ID = "101"
    }

    fun SyncData(): Int{
        val supportURL = "http://www.bizinfo.go.kr/uss/rss/bizPersonaRss.do?dataType=json"
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

        try{
            val start = System.currentTimeMillis()

            //서버 연결
            val requestURLConnection = RequestURLConnection(supportURL)
            val line = requestURLConnection.DataLoad()

            val json:JSONObject? = JSONObject(line)
            val jsonArray: JSONArray = json?.getJSONArray("jsonArray")!!

            val db: AppDatabase = Room.databaseBuilder(context,AppDatabase::class.java,"app_db").build()
            //동기화 시간
            val permit: PermitModel = db.permitDAO().getItem()
            val alert = db.permitDAO().getAll()
            val sync: Date = simpleDateFormat.parse(permit.syncTime)

            for(i in 0 until jsonArray.length()){
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val supportItem: SupportModel = JsonParsing_support(jsonObject)

                val create: Date= simpleDateFormat.parse(supportItem.creatPnttm)
                val differentTime: Long = sync.time - create.time
                val differentDay: Long = differentTime/(24*60*60*1000)

                //시간 차이가 2 이하이면 새로 생긴 게시글
                if(differentDay<=2){
                    supportItem.checkNew = true
                    notificationNewSupport(i,supportItem.pblancId,supportItem.pblancNm!!)
                }

                //db에 insert
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

    fun notificationNewSupport(NOTIFICATION_ID:Int,id:String,title:String){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context,SupportActivity::class.java)
        notificationIntent.putExtra("newPostID",id)
        notificationIntent.putExtra("newCheck",true)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        //OREO notification 채널
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            val channelName = "Notification Channel"
            val description = "Oreo"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID,channelName,importance)
            channel.description = description

            notificationManager.createNotificationChannel(channel)
        }else
            builder.setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(NOTIFICATION_ID,builder.build())



    }

}