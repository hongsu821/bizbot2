package com.bizbot.bizbot2.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_word")
data class SearchWordModel(
    @PrimaryKey var word: String
)