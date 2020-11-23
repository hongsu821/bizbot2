package com.bizbot.bizbot2.room

import android.content.Context
import androidx.room.Database
import androidx.room.PrimaryKey
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

@Database(entities = [SupportModel::class, SearchWordModel::class, PermitModel::class, UserModel::class], version = 1, exportSchema = false)
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
                        //.addMigrations(MIGRATION_5_6)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }

        val MIGRATION_4_5 = object: Migration(4,5){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE 'new_permit' ('id' INTEGER PRIMARY KEY NOT NULL, 'alert' INTEGER, 'syncTime' TEXT, " +
                        "'keyword' TEXT, 'field' INTEGER, 'subclass' INTEGER)")
                database.execSQL("DROP TABLE permit")
                database.execSQL("ALTER TABLE new_permit RENAME TO permit")
            }
        }

        val MIGRATION_5_6 = object: Migration(5,6){
            override fun migrate(database: SupportSQLiteDatabase) {
                //database.execSQL("ALTER TABLE user ADD COLUMN name TEXT, ")
                database.execSQL("CREATE TABLE 'new_user' ('id' INTEGER PRIMARY KEY NOT NULL, 'business_type' INTEGER,'business_name' TEXT, 'name' TEXT," +
                        " 'gender' INTEGER, 'birth' TEXT, 'business_category' INTEGER, 'area' INTEGER, 'city' INTEGER)")
                database.execSQL("INSERT INTO 'new_user' ('id','business_type','business_name','name','gender','birth','business_category','area','city')" +
                        "SELECT 'id','business_type','business_name','name','gender','birth','business_category','area','city' FROM 'user'")
                database.execSQL("DROP TABLE user")
                database.execSQL("ALTER TABLE new_user RENAME TO user")
            }
        }

    }



}