package com.bizbot.bizbot2.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bizbot.bizbot2.room.model.PermitModel

@Dao
interface PermitDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(permitModel: PermitModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(permitModel: PermitModel)

    @Query("UPDATE permit SET alert = :check")
    fun setAlert(check:Boolean)

    @Query("UPDATE permit SET syncTime = :time")
    fun setSyncTime(time: String)

    @Query("UPDATE permit SET area=:area")
    fun setArea(area:String)

    @Query("SELECT area FROM permit WHERE id=0")
    fun getArea(): LiveData<String>

    @Query("UPDATE permit SET area_id=:id")
    fun setAreaID(id:Int)

    @Query("UPDATE permit SET city=:city")
    fun setCity(city:String)

    @Query("UPDATE permit SET city_id=:id")
    fun setCityID(id:Int)

    @Query("SELECT * FROM permit")
    fun getAll(): LiveData<PermitModel>

    @Query("SELECT alert FROM permit")
    fun isAlertCheck(): Boolean

    @Query("SELECT * FROM permit")
    fun getItem(): PermitModel



}