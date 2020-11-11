package com.bizbot.bizbot2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bizbot.bizbot2.room.dao.PermitDAO
import com.bizbot.bizbot2.room.dao.SearchWordDAO
import com.bizbot.bizbot2.room.dao.SupportDAO
import com.bizbot.bizbot2.room.dao.UserModelDAO
import com.bizbot.bizbot2.room.model.PermitModel
import com.bizbot.bizbot2.room.model.SearchWordModel
import com.bizbot.bizbot2.room.model.SupportModel
import com.bizbot.bizbot2.room.model.UserModel

@Database(entities = [SupportModel::class, SearchWordModel::class, PermitModel::class, UserModel::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun supportDAO(): SupportDAO
    abstract fun searchDAO(): SearchWordDAO
    abstract fun permitDAO(): PermitDAO
    abstract fun userDAO(): UserModelDAO


    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase?{
            if(INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_db")//.fallbackToDestructiveMigration().build()
                        //.addMigrations(MIGRATION_1_2)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }

        val MIGRATION_1_2 = object: Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE 'new_user' ('id' INTEGER PRIMARY KEY NOT NULL, 'business_type' INTEGER, 'establishment' TEXT, " +
                        "'name' TEXT, 'gender' INTEGER, 'birth' TEXT, 'business_category' INTEGER, 'area' INTEGER, 'city' INTEGER)")
                database.execSQL("DROP TABLE user")
                database.execSQL("ALTER TABLE new_user RENAME TO user")
            }
        }

        val MIGRATION_2_3 = object: Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                //database.execSQL("ALTER TABLE user ADD COLUMN name TEXT, ")
                database.execSQL("INSERT INTO user (id) VALUES(0)")
            }
        }

    }



}