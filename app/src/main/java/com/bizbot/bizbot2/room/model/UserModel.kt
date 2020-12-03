package com.bizbot.bizbot2.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "user")
class UserModel(
    @PrimaryKey @NotNull var id:Int,
    @ColumnInfo(name = "business_type") var businessType: String?, //사업자 유형
    @ColumnInfo(name = "business_name") var businessName: String?, //사업체명
    var name: String?, //대표자 이름
    var gender: String?, //성별
    var birth: String?, //생년월일
    @ColumnInfo(name = "business_category_id") var businessCategoryNum: Int?, //업종 id
    @ColumnInfo(name = "business_category") var businessCategory: String?, //업종
    @ColumnInfo(name = "field_id") var fieldNum:Int?, //지원 받고 싶은 분야 id
    var field: String?, //분야
    @ColumnInfo(name = "subclass_id") var subclassNum: Int?, // 분야 하위 카테고리 id
    var subclass: String? //분야 하위 카테고리
)