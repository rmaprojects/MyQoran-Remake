package com.rmaproject.myqoran.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rmaproject.myqoran.database.dao.BookmarkDAO
import com.rmaproject.myqoran.database.model.Bookmark


@Database(
    entities = [Bookmark::class],
    version = 1
)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDAO

    companion object {

        @Volatile private var INSTANCE: BookmarkDatabase? = null
        fun getInstance(context:Context) : BookmarkDatabase {
            return INSTANCE?: synchronized(this) {
                INSTANCE ?: BookmarkDatabase.buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }
        private fun buildDatabase(context: Context): BookmarkDatabase {
            return Room.databaseBuilder(context.applicationContext, BookmarkDatabase::class.java, "bookmark.db")
                .fallbackToDestructiveMigration()
                .build()
        }

    }
}