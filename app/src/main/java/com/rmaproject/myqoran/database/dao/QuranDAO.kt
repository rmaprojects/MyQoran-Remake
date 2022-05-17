package com.rmaproject.myqoran.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.rmaproject.myqoran.database.model.Juz
import com.rmaproject.myqoran.database.model.Page
import com.rmaproject.myqoran.database.model.Quran
import com.rmaproject.myqoran.database.model.Surah
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
}