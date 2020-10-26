package com.bizbot.bizbot2.room

import androidx.lifecycle.LiveData
import com.bizbot.bizbot2.room.dao.SupportDAO
import com.bizbot.bizbot2.room.model.SupportModel

class SupportRepository(private val supportDAO: SupportDAO) {

    val allList: LiveData<List<SupportModel>> = supportDAO.getAllList()

    suspend fun insert(supports: SupportModel){
        supportDAO.insert(supports)
    }

}