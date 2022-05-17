package com.rmaproject.myqoran.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran")
data class Quran(
    @PrimaryKey val id : Int? = 0,
    @ColumnInfo(name = "jozz") val juzNumber : Int?,
    @ColumnInfo(name = "sora") val surahNumber : Int? = 0, //Surah Number
    @ColumnInfo(name = "sora_name_en") val SurahName_en : String? = "",
    @ColumnInfo(name = "sora_name_ar") val SurahName_ar : String? = "",
    @ColumnInfo(name = "page") val page : Int? = 0,
    @ColumnInfo(name = "line_start") val lineStart: Int? = 0,
    @ColumnInfo(name = "line_end") val lineEnd:Int? = 0,
    @ColumnInfo(name = "aya_no") val AyahNumber : Int? = 0,
    @ColumnInfo(name = "aya_text") val TextQuran: String? = "",
    @ColumnInfo(name = "aya_text_emlaey") val textQuranSearch:String? = "",
    @ColumnInfo(name = "sora_name_id") val SurahName_id:String? = "",
    @ColumnInfo(name = "sora_descend_place") val turunSurah:String? = "",
    @ColumnInfo(name = "sora_name_emlaey") val textSurahSearch:String? = "",
    val translation_id:String? = "",
    val footnotes_id:String? = "",
    val translation_en:String? = "",
    val footnotes_en:String? = "",
)
