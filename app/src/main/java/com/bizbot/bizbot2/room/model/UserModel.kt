package com.bizbot.bizbot2.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "user")
class UserModel(
    @PrimaryKey @NotNull var id:Int,
    @ColumnInfo(name = "business_type_id") var businessTypeNum: Int?, //사업자 유형 id
    @ColumnInfo(name = "business_type") var businessType: String?, //사업자 유형
    @ColumnInfo(name = "business_name") var businessName: String?, //사업체명
    var name: String?, //대표자 이름
    @ColumnInfo(name = "gender_id") var genderNum: Int?, //성별 id
    var gender: String?, //성별
    var birth: String?, //생년월일
    @ColumnInfo(name = "business_category_id") var businessCategoryNum: Int?, //업종 id
    @ColumnInfo(name = "business_category") var businessCategory: String?, //업종
    @ColumnInfo(name = "area_id") var areaNum: Int?, //지역 id (서울,대구,경주...)
    var area: String?, //지역
    @ColumnInfo(name = "city_id") var cityNum: Int?, //도시 id
    var city: String? //도시 (서초구, 동구 ...)
)