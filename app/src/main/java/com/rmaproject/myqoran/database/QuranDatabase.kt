package com.rmaproject.myqoran.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.dao.QuranDAO
import com.rmaproject.myqoran.database.model.*

@Database(entities = [Quran::class], views = [Surah::class, Juz::class, Page::class, AyahTerakhirFinderPage::class], version = 1)
abstract class QuranDatabase : RoomDatabase(){
    abstract fun quranDao(): QuranDAO

    companion object {

        @Volatile private var INSTANCE: QuranDatabase? = null
        fun getInstance(context: Context): QuranDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): QuranDatabase {
            return Room.databaseBuilder(context.applicationContext, QuranDatabase::class.java, "quran.db")
                .createFromInputStream {
                    context.resources.openRawResource(R.raw.quran)
                }
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
