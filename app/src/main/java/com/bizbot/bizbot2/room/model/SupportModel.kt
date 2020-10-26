package com.bizbot.bizbot2.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "support")
data class SupportModel(
    @PrimaryKey var pblancId: String,
    var industNm: String?,
    var rceptInsttEmailAdres: String?,
    var inqireCo: Int?,   //조회수
    var rceptEngnHmpgUrl: String?,
    var pblancUrl: String?,
    var jrsdInsttNm: String?,
    var rceptEngnNm: String?,
    var entrprsStle: String?,
    var pldirSportRealmLclasCodeNm: String?,
    var trgetNm: String?,
    var rceptInsttTelno: String?,
    var bsnsSumryCn: String?,
    var reqstBeginEndDe: String?,
    var areaNm: String?,
    var pldirSportRealmMlsfcCodeNm: String?,
    var rceptInsttChargerDeptNm: String?,
    var rceptInsttChargerNm: String?,
    var pblancNm: String?,
    var creatPnttm: String?,
    var checkLike: Boolean?,
    var checkNew: Boolean?
)