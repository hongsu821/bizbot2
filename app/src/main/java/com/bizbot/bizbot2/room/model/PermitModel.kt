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
    @ColumnInfo(name = "field_id") var fieldNum:Int?,
    var field: String?,
    @ColumnInfo(name = "subclass_id") var subclassNum: Int?,
    var subclass: String?
)
