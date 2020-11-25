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
    @ColumnInfo(name = "area_id") var areaNum: Int?, //지역 id (서울,대구,경주...)
    var area: String?, //지역
    @ColumnInfo(name = "city_id") var cityNum: Int?, //도시 id
    var city: String? //도시 (서초구, 동구 ...)
)
