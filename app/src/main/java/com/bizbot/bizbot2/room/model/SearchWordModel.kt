package com.bizbot.bizbot2.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "search_word")
data class SearchWordModel(
    @PrimaryKey @NotNull var word: String
)