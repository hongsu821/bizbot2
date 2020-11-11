package com.bizbot.bizbot2.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bizbot.bizbot2.room.AppRepository
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.room.model.UserModel

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val users = repository.getAllUser()

    fun getAllUser(): LiveData<UserModel>{
        return users
    }

    fun setUser(users: UserModel){
        repository.setUser(users)
    }

    fun insertUser(users: UserModel){
        repository.insertUser(users)
    }
}