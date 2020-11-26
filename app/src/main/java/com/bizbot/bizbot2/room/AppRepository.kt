package com.bizbot.bizbot2.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.bizbot.bizbot2.room.dao.PermitDAO
import com.bizbot.bizbot2.room.dao.SearchWordDAO
import com.bizbot.bizbot2.room.dao.SupportDAO
import com.bizbot.bizbot2.room.dao.UserModelDAO
import com.bizbot.bizbot2.room.model.PermitModel
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.room.model.UserModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception
import java.lang.Runnable
import java.util.*

class AppRepository(application: Application) {

    private val supportDAO: SupportDAO by lazy{
        val db = AppDatabase.getInstance(application)!!
        db.supportDAO()
    }
    private val searchWordDAO: SearchWordDAO by lazy {
        val db = AppDatabase.getInstance(application)!!
        db.searchDAO()
    }
    private val permitDAO: PermitDAO by lazy {
        val db = AppDatabase.getInstance(application)!!
        db.permitDAO()
    }
    private val userModelDAO: UserModelDAO by lazy{
        val db = AppDatabase.getInstance(application)!!
        db.userDAO()
    }


    //지원사업 전부 출력
    fun getAllSupports(): LiveData<List<SupportModel>>{
        return supportDAO.getAllList()
    }
    //지원사업 입력
    fun insertSupport(supports: SupportModel){
        CoroutineScope(Dispatchers.IO).launch {
            supportDAO.insert(supports)
        }

    }
    //새 게시글 설정
    fun setNew(check: Boolean, id: String){
        CoroutineScope(Dispatchers.IO).launch {
            supportDAO.setNew(check,id)
        }
    }


    //알림 설정 입력
    fun insertPermit(permitModel: PermitModel){
        CoroutineScope(Dispatchers.IO).launch {
            permitDAO.insert(permitModel)
        }
    }
    //알림 설정 업데이트
    fun updatePermit(permitModel: PermitModel){
        CoroutineScope(Dispatchers.IO).launch {
            permitDAO.update(permitModel)
        }

    }
    //알림 설정 전체 출력
    fun getAllPermit():LiveData<PermitModel>{
        return permitDAO.getAll()
    }
    //지역 설정
    fun setArea(area:String,areaID:Int,city:String,cityID:Int){
        CoroutineScope(Dispatchers.IO).launch {
            permitDAO.setArea(area)
            permitDAO.setAreaID(areaID)
            permitDAO.setCity(city)
            permitDAO.setCityID(cityID)
        }
    }

    //관심사업 전부 출력
    fun getLikeList(): LiveData<List<SupportModel>>{
        return supportDAO.getLikeList()
    }
    //관심사업 설정
    fun setLike(check: Boolean, id: String){
        CoroutineScope(Dispatchers.IO).launch {
            supportDAO.setLike(check,id)
        }
    }


    //검색어 전부 출력
    fun getAllSearch(): LiveData<List<String>>{
        return searchWordDAO.getAllList()
    }
    //검색어 하나만 출력
    fun getItemSearch(word: String): LiveData<String>{
        return searchWordDAO.getItem(word)
    }
    //검색어 입력
    fun insertSearch(word: SearchWordModel){
        CoroutineScope(Dispatchers.IO).launch {
            searchWordDAO.insert(word)
        }
    }
    //검색어 전체 삭제
    fun delSearchAll(){
        CoroutineScope(Dispatchers.IO).launch {
            searchWordDAO.deleteAll()
        }
    }
    //검색어 하나만 삭제
    fun delSearchItem(word: String){
        CoroutineScope(Dispatchers.IO).launch {
            searchWordDAO.deleteItem(word)
        }
    }


    //유저 정보 전부 출력
    fun getAllUser(): LiveData<UserModel>{
        return userModelDAO.getAllItem()
    }
    //유저 정보 수정
    fun setUser(users: UserModel){
        CoroutineScope(Dispatchers.IO).launch {
            userModelDAO.update(users)
        }
    }
    //유저 정보 입력
    fun insertUser(users: UserModel){
        CoroutineScope(Dispatchers.IO).launch {
            userModelDAO.insert(users)
        }
    }

}