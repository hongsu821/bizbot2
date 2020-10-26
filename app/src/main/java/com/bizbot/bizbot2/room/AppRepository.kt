package com.bizbot.bizbot2.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.bizbot.bizbot2.room.dao.SupportDAO
import com.bizbot.bizbot2.room.model.SupportModel
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

    fun getLikeList(): LiveData<List<SupportModel>>{
        return supportDAO.getLikeList()
    }

    /*fun insertSupport(supportModel: SupportModel): Observable{
        return Observable.fromCallable{ supportDAO.insert(supportModel)}
    }*/

    //suspend : 코루틴에서 지연되었다가 재개 될수 있는 함수
    suspend fun insert(supports: SupportModel){
        supportDAO.insert(supports)
    }


}