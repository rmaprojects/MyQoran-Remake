package com.rmaproject.myqoran.ui.settings.preferences

import com.chibatching.kotpref.KotprefModel

object SettingsPreferences : KotprefModel() {

    var isDarkMode by booleanPref(false)
    var currentTheme by intPref(0)
    var languagePreference by intPref(0)
    var isOnFocusRead by booleanPref(false)
    var showTajweed by booleanPref(true)
    var ayahSizePreference by floatPref(35F)
    var isUsingLiveLocation by booleanPref(true)

    override fun clear() {
        super.clear()
        isDarkMode = false
        currentTheme = 0
        languagePreference = 0
        isOnFocusRead = false
        showTajweed = true
        ayahSizePreference = 35F
        isUsingLiveLocation = true
    }
}