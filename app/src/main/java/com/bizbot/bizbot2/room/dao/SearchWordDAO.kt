package com.bizbot.bizbot2.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bizbot.bizbot2.room.model.SearchWordModel

@Dao
interface SearchWordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchWordModel: SearchWordModel)

    @Query("DELETE FROM search_word")
    fun deleteAll();

    @Query("DELETE FROM search_word WHERE word = :word")
    fun deleteItem(word: String)

    @Query("SELECT * FROM search_word")
    fun getAllList(): LiveData<List<String>>

    @Query("SELECT * FROM search_word WHERE word = :word")
    fun getItem(word: String): LiveData<String>
}