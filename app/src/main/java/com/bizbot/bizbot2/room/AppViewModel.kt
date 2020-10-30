package com.bizbot.bizbot2.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizbot.bizbot2.room.model.SupportModel

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val supports = repository.getAllSupports()

    fun getAllSupport(): LiveData<List<SupportModel>>{
        return this.supports
    }

    fun insertSupport(support: SupportModel){
        repository.insertSupport(support)
    }

    fun setNew(check: Boolean, id: String){
        repository.setNew(check,id)
    }

    fun setLike(check: Boolean, id: String){
        repository.setLike(check,id)
    }

    fun getLikeList(){
        repository.getLikeList()
    }



}