package com.bizbot.bizbot2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bizbot.bizbot2.room.dao.PermitDAO
import com.bizbot.bizbot2.room.dao.SearchWordDAO
import com.bizbot.bizbot2.room.dao.SupportDAO
import com.bizbot.bizbot2.room.model.PermitModel
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel

@Database(entities = [SupportModel::class, SearchWordModel::class, PermitModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun supportDAO(): SupportDAO
    abstract fun searchDAO(): SearchWordDAO
    abstract fun permitDAO(): PermitDAO

    //companion : java의 static 같은거
    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase?{
            if(INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }

}