package com.bizbot.bizbot2.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bizbot.bizbot2.room.model.PermitModel
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.room.model.UserModel

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val supports = repository.getAllSupports()
    private val likeList = repository.getLikeList()
    private val searchWords = repository.getAllSearch()
    private val users = repository.getAllUser()
    private val permits = repository.getAllPermit()


    fun getAllSupport(): LiveData<List<SupportModel>>{
        return this.supports
    }

    fun insertSupport(support: SupportModel){
        repository.insertSupport(support)
    }

    fun setNew(check: Boolean, id: String){
        repository.setNew(check,id)
    }

    fun insertPermit(permitModel: PermitModel){
        repository.insertPermit(permitModel)
    }

    fun updatePermit(permitModel: PermitModel){
        repository.updatePermit(permitModel)
    }

    fun getAllPermit(): LiveData<PermitModel>{
        return this.permits
    }

    fun setLike(check: Boolean, id: String){
        repository.setLike(check,id)
    }

    fun getLikeList(): LiveData<List<SupportModel>>{
        return likeList
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

    fun getAllUser(): LiveData<UserModel>{
        return users
    }

    fun setUser(users: UserModel){
        repository.setUser(users)
    }

    fun insertUser(users: UserModel){
        repository.insertUser(users)
    }

    fun setArea(area:String,areaID:Int,city:String,cityID:Int){
        repository.setArea(area,areaID,city,cityID)
    }

    fun getArea():LiveData<String>{
        return repository.getArea()
    }

    fun getUserName():LiveData<String>{
        return repository.getUserName()
    }
}