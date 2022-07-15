package com.rmaproject.myqoran.ui.settings.preferences

import com.chibatching.kotpref.KotprefModel

object RecentReadPreferences: KotprefModel() {
    var lastReadSurah by stringPref("Belum Baca")
    var lastReadAyah by stringPref("")
    var lastReadSurahNumber by intPref(1)
    var lastReadPosition by intPref(0)
    var position by intPref(0)
    var lastReadJuzNumber by intPref(1)
    var lastReadPageNumber by intPref(1)
}