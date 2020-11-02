package com.bizbot.bizbot2.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val supports = repository.getAllSupports()
    private val searchWords = repository.getAllSearch()

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

    fun getAllSearch(): LiveData<List<String>>{
        return this.searchWords
    }

    fun getSearchItem(word: String): LiveData<String>{
        return repository.getItemSearch(word)
    }

    fun insertSearch(word: SearchWordModel){
        repository.insertSearch(word)
    }

    fun delSearchItem(word: String){
        repository.delSearchItem(word)
    }

    fun delSearchAll(){
        repository.delSearchAll()
    }


}