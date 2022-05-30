package com.rmaproject.myqoran.database.model

import androidx.room.DatabaseView

@DatabaseView("SELECT page FROM  quran GROUP by page")
data class GetPageNames(
    val page: Int? = 0
)
