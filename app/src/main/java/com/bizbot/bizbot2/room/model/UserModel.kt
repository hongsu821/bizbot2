package com.bizbot.bizbot2.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "user")
class UserModel(
    @PrimaryKey @NotNull var id:Int,
    @ColumnInfo(name = "business_type") var businessType: Int?, //사업자 유형
    @ColumnInfo(name = "business_name") var businessName: String?, //사업체명
    var name: String?, //대표자 이름
    var gender: Int?, //성별
    var birth: String?, //생년월일
    @ColumnInfo(name = "business_category") var businessCategory: Int?, //업종
    var area: Int?, //지역 (서울,대구,경주...)
    var city: Int? //도시 (서초구, 동구 ...)
)