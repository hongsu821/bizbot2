package com.bizbot.bizbot2.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bizbot.bizbot2.room.model.SupportModel

@Dao
interface SupportDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(supportModel: SupportModel)

    @Delete
    fun delete(supportModel: SupportModel)

    @Query("DELETE FROM support")
    fun deleteAll()

    //모든 데이터 출력
    @Query("SELECT * FROM support")
    fun getAllList(): LiveData<List<SupportModel>>

    //신규 지원사업 체크
    @Query("UPDATE support SET checkNew = :check WHERE pblancId=:id")
    fun setNew(check: Boolean,id: String)

    //관심 사업 체크
    @Query("UPDATE support SET checkLike = :check WHERE pblancId=:id")
    fun setLike(check: Boolean,id: String)

    //관심 사업 출력
    @Query("SELECT * FROM support WHERE checkLike = 1")
    fun getLikeList(): LiveData<List<SupportModel>>



}