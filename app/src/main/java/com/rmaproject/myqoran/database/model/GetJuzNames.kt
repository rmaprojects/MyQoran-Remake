package com.rmaproject.myqoran.database.model

import androidx.room.DatabaseView

@DatabaseView("SELECT jozz FROM  quran GROUP by jozz")
data class GetJuzNames(
    val jozz: Int? = 0
)
