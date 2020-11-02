package com.bizbot.bizbot2.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.bizbot.bizbot2.room.dao.SearchWordDAO
import com.bizbot.bizbot2.room.dao.SupportDAO
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel
import java.lang.Exception
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

    private val supports: LiveData<List<SupportModel>> by lazy { supportDAO.getAllList() }
    private val searchWords: LiveData<List<String>> by lazy { searchWordDAO.getAllList() }

    //지원사업 전부 출력
    fun getAllSupports(): LiveData<List<SupportModel>>{
        return supportDAO.getAllList()
    }
    //지원사업 입력
    fun insertSupport(supports: SupportModel){
        try{
            val thread = Thread(Runnable {
                supportDAO.insert(supports)
            })
            thread.start()
        }catch (e: Exception){e.printStackTrace()}
    }

    //새 게시글 설정
    fun setNew(check: Boolean, id: String){
        try{
            val thread = Thread(Runnable {
                supportDAO.setNew(check,id)
            })
            thread.start()
        }catch (e: Exception){e.printStackTrace()}
    }

    //관심사업 전부 출력
    fun getLikeList(): LiveData<List<SupportModel>>{
        return supportDAO.getLikeList()
    }
    //관심사업 설정
    fun setLike(check: Boolean, id: String){
        try{
            val thread = Thread(Runnable {
                supportDAO.setLike(check,id)
            })
            thread.start()
        }catch (e: Exception){e.printStackTrace()}
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
        try{
            val thread = Thread(Runnable {
                searchWordDAO.insert(word)
            })
            thread.start()
        }catch (e: Exception){e.printStackTrace()}
    }
    //검색어 전체 삭제
    fun delSearchAll(){
        try{
            val thread = Thread(Runnable {
                searchWordDAO.deleteAll()
            })
            thread.start()
        }catch (e: Exception){e.printStackTrace()}
    }
    //검색어 하나만 삭제
    fun delSearchItem(word: String){
        try{
            val thread = Thread(Runnable {
                searchWordDAO.deleteItem(word)
            })
            thread.start()
        }catch (e: Exception){e.printStackTrace()}
    }

}