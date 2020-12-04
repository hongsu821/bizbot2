package com.bizbot.bizbot2.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.bizbot.bizbot2.R
import com.bizbot.bizbot2.home.IntroActivity
import com.bizbot.bizbot2.room.AppDatabase
import com.bizbot.bizbot2.room.model.PermitModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.room.model.UserModel
import com.bizbot.bizbot2.support.SupportDetailActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SynchronizationData(var context: Context) {
    companion object{
        private val TAG = "SynchronizationData"
        val CHANNEL_ID = "101"
    }



    fun syncData(): Int{
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
            //지난 지원사업 삭제용
            db.supportDAO().deleteAll()
            //알림설정에 필요한 정보
            val permit: PermitModel = db.permitDAO().getItem()
            val userInfo = db.userDAO().getItem()
            //동기화 시간
            val sync: Date = simpleDateFormat.parse(permit.syncTime)

            for(i in 0 until jsonArray.length()){
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val supportItem: SupportModel = JsonParsing_support(jsonObject)

                val create: Date= simpleDateFormat.parse(supportItem.creatPnttm)
                val differentTime: Long = sync.time - create.time
                val differentDay: Long = differentTime/(24*60*60*1000)

                //시간 차이가 2 이하이면 새로 생긴 게시글
                supportItem.checkNew = differentDay<=2

                //지역 알림
                if(sync < create && permit.alert!!)
                    notificationSetting(i,supportItem,permit,userInfo)

                //db에 insert
                db.supportDAO().insert(supportItem)
            }

            val end = System.currentTimeMillis()
            Log.d(TAG, "data loading: "+(end-start)/1000+" s")

            //동기화 시간
            val syncDate = Date(System.currentTimeMillis())
            db.permitDAO().setSyncTime(simpleDateFormat.format(syncDate))

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

    private fun JsonParsing_support(jsonObject: JSONObject): SupportModel{
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

    //지역 알림용 단어 포맷 변경
    private fun changeArea(beforeArea:String):String{
        var afterArea = ""
        when(beforeArea){
            "서울특별시" -> afterArea = "서울"
            "부산광역시" -> afterArea = "부산"
            "대구광역시" -> afterArea = "대구"
            "인천광역시" -> afterArea = "인천"
            "광주광역시" -> afterArea = "광주"
            "대전광역시" -> afterArea = "대전"
            "울산광역시" -> afterArea = "울산"
            "세종특별자치시" -> afterArea = "세종"
            "강원도" -> afterArea = "강원"
            "경기도" -> afterArea = "경기"
            "충청북도" -> afterArea = "충북"
            "충청남도" -> afterArea = "충남"
            "전라북도" -> afterArea = "전북"
            "전라남도" -> afterArea = "전남"
            "경상남도" -> afterArea = "경남"
            "경상북도" -> afterArea = "경북"
            "제주특별자치도" -> afterArea = "제주"
        }
        return afterArea
    }

    //키워드 알림, 지역 알림
    private fun notificationSetting(num:Int,support:SupportModel,permit:PermitModel,userInfo:UserModel){
        val words = permit.keyword?.split("@")

        //제목에 '지역' 단어가 들어가는지
        if(support.pblancNm?.contains(changeArea(permit.area!!))!!)
            notificationNewSupport(num,support)
        //본문에 사용자설정키워드가 들어가는지
        for(word in words!!){
            if (word == "")
                continue
            if(support.bsnsSumryCn?.contains(word)!!){//본문에 키워드 포함
                notificationNewSupport(num,support)
                break
            }
        }
        //지원 받고 싶은 분야 선택시
        if(userInfo.fieldNum != 0 && userInfo.subclassNum == 0){
            if(support.pldirSportRealmLclasCodeNm?.contains(userInfo.field!!)!!)
                notificationNewSupport(num,support)
        }
        //분야 하위 클래스
        if(userInfo.subclassNum != 0 && support.pldirSportRealmLclasCodeNm?.contains(userInfo.subclass!!)!!)
            notificationNewSupport(num,support)

    }

    private fun notificationNewSupport(NOTIFICATION_ID:Int, support: SupportModel){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //클릭시 이동할 액티비티
        val notificationIntent = Intent(context,SupportDetailActivity::class.java)
        //전달할 값
        notificationIntent.putExtra("detail",support)
        notificationIntent.putExtra("notiCheck",1)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(support.pblancNm)
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