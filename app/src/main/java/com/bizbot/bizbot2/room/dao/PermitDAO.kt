package com.bizbot.bizbot2.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bizbot.bizbot2.room.model.PermitModel

@Dao
interface PermitDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(permitModel: PermitModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(permitModel: PermitModel)

    @Query("UPDATE permit SET alert = :check")
    fun setAlert(check:Boolean)

    @Query("UPDATE permit SET syncTime = :time")
    fun setSyncTime(time: String)

    @Query("SELECT * FROM permit WHERE id = 1")
    fun getAll(): LiveData<PermitModel>

    @Query("SELECT alert FROM permit WHERE id = 1")
    fun isAlertCheck(): Boolean

    @Query("SELECT * FROM permit")
    fun getItem(): PermitModel
}