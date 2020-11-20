package com.bizbot.bizbot2.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bizbot.bizbot2.room.model.UserModel

@Dao
interface UserModelDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: UserModel)

    @Delete
    fun delete(user: UserModel)

    //모든 데이터 출력
    @Query("SELECT * FROM user")
    fun getAllItem(): LiveData<UserModel>

    @Query("SELECT * FROM user")
    fun getItem(): UserModel



}