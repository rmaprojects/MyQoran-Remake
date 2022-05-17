package com.rmaproject.myqoran.database.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.PrimaryKey

@DatabaseView("SELECT MAX(id) as id, page, sora, aya_no, aya_text, sora_name_en, sora_name_ar FROM quran GROUP by page ORDER BY id")

data class AyahTerakhirFinderPage(

    @PrimaryKey val id:Int? = 0,
    @ColumnInfo(name = "page") val page : Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber : Int? = 0,
    @ColumnInfo(name = "aya_no") val AyahNumber : Int? = 0,
    @ColumnInfo(name = "aya_text") val TextQuran: String? = "",
    @ColumnInfo(name = "sora_name_en") val SurahName_en : String? = "",
    @ColumnInfo(name = "ayah_total") val numberOfAyah:Int? = 0,
    @ColumnInfo(name = "sora_name_ar") val SurahName_ar : String? = ""
)