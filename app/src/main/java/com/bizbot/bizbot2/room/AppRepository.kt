package com.bizbot.bizbot2.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.bizbot.bizbot2.room.dao.SupportDAO
import com.bizbot.bizbot2.room.model.SupportModel
import java.lang.Exception
import java.util.*

class AppRepository(application: Application) {

    private val supportDAO: SupportDAO by lazy{
        val db = AppDatabase.getInstance(application)!!
        db.supportDAO()
    }

    private val supports: LiveData<List<SupportModel>> by lazy {
        supportDAO.getAllList()
    }

    fun getAllSupports(): LiveData<List<SupportModel>>{
        return supportDAO.getAllList()
    }

    fun insertSupport(supports: SupportModel){
        try{
            val thread = Thread(Runnable {
                supportDAO.insert(supports)
            })
            thread.start()
        }catch (e: Exception){e.printStackTrace()}
    }

    fun setNew(check: Boolean, id: String){
        try{
            val thread = Thread(Runnable {
                supportDAO.setNew(check,id)
            })
            thread.start()
        }catch (e: Exception){e.printStackTrace()}
    }

    //관심사업 출력
    fun getLikeList(): LiveData<List<SupportModel>>{
        return supportDAO.getLikeList()
    }

    fun setLike(check: Boolean, id: String){
        try{
            val thread = Thread(Runnable {
                supportDAO.setLike(check,id)
            })
            thread.start()
        }catch (e: Exception){e.printStackTrace()}
    }




}