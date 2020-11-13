package com.bizbot.bizbot2.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "permit")
data class PermitModel(
    @PrimaryKey var id: Int,
    var alert: Boolean?,
    var syncTime: String?,
    var keyword: String?,
    var field: Int?,
    var subclass: Int?
)
