package com.rmaproject.myqoran.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.rmaproject.myqoran.database.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDAO {
    @Query("SELECT * FROM Surah")
    fun showQuranIndexBySurah(): Flow<List<Surah>>

    @Query("SELECT * FROM Juz")
    fun showQuranIndexByJuz(): Flow<List<Juz>>

    @Query("SELECT * FROM Page")
    fun showQuranIndexByPage(): Flow<List<Page>>

    @Query("SELECT * FROM quran WHERE sora = :soraNumber")
    fun showReadQuran(soraNumber :Int) : Flow<List<Quran>>

    @Query("SELECT * FROM LastAyahFinderPage")
    fun getLastAyahOfPage() : Flow<List<LastAyahFinderPage>>

    @Query("SELECT * FROM LastAyahFinderJuz")
    fun getLastSurahOfJuz() : Flow<List<LastAyahFinderJuz>>

    @Query("SELECT sora, jozz, aya_no, aya_text, aya_text_emlaey, translation_id, sora_name_ar, sora_name_en, footnotes_id, translation_en, footnotes_en, sora_descend_place FROM quran WHERE sora = :surahNumber")
    fun readQuranBySurah(surahNumber:Int):Flow<List<Quran>>

    @Query("SELECT sora, jozz, aya_no, aya_text, aya_text_emlaey, translation_id, sora_name_ar, sora_name_en, footnotes_id, translation_en, footnotes_en, sora_descend_place FROM quran WHERE jozz = :juzNumber")
    fun readQuranByJuz(juzNumber:Int):Flow<List<Quran>>

    @Query("SELECT sora, jozz, aya_no, aya_text, aya_text_emlaey, translation_id, sora_name_ar, sora_name_en, footnotes_id, translation_en, footnotes_en, sora_descend_place FROM quran WHERE aya_no = :pageNumber")
    fun readQuranByPage(pageNumber:Int):Flow<List<Quran>>

    @Query("SELECT * FROM GetSurahNames")
    fun getSurahNames(): Flow<List<GetSurahNames>>

    @Query("SELECT * FROM GetJuzNames")
    fun getJuzNames(): Flow<List<GetJuzNames>>

    @Query("SELECT * FROM GetPageNames")
    fun getPageNames(): Flow<List<GetPageNames>>

}