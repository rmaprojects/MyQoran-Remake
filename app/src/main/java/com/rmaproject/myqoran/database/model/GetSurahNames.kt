package com.rmaproject.myqoran.database.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT sora_name_en, COUNT(aya_no) as 'total' FROM  quran GROUP by sora")
data class GetSurahNames(
    @ColumnInfo(name = "sora_name_en") val surahName:String? = "",
    @ColumnInfo(name = "total") val total:Int? = 0
)
