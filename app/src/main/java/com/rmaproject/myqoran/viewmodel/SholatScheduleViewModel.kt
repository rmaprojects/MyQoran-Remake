package com.rmaproject.myqoran.viewmodel

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class SholatScheduleViewModel : ViewModel() {
    var shubuhTime: String? = null
    var dzuhurTime: String? = null
    var asharTime: String? = null
    var maghribTime: String? = null
    var isyaTime: String? = null

    fun getGapBetweenTimes(currentTime: List<String>): String {
        val convertedCurrentTime = getCalendarTime(currentTime)
        val shubuhState = shubuhTime?.let { time -> getShubuhState(convertedCurrentTime, time) }
        val dzuhurState = dzuhurTime?.let { time -> getCurrentState(convertedCurrentTime, time) }
        val asharState = asharTime?.let { time -> getCurrentState(convertedCurrentTime, time) }
        val maghribState = maghribTime?.let { time -> getCurrentState(convertedCurrentTime, time) }
        val isyaState = isyaTime?.let { time -> getCurrentState(convertedCurrentTime, time) }

        return when {
            shubuhState == true -> {
                "${
                    gapCounter(
                        convertedCurrentTime,
                        getCalendarTime(shubuhTime!!.split(":"))
                    )
                } lagi menuju sholat Shubuh"
            }
            dzuhurState == true -> {
                "${
                    gapCounter(
                        convertedCurrentTime,
                        getCalendarTime(dzuhurTime!!.split(":"))
                    )
                } lagi menuju sholat Dzuhur"
            }
            asharState == true -> {
                "${
                    gapCounter(
                        convertedCurrentTime,
                        getCalendarTime(asharTime!!.split(":"))
                    )
                } lagi menuju sholat Ashar"
            }
            maghribState == true -> {
                "${
                    gapCounter(
                        convertedCurrentTime,
                        getCalendarTime(maghribTime!!.split(":"))
                    )
                } lagi menuju sholat Maghrib"
            }
            isyaState == true -> {
                "${
                    gapCounter(
                        convertedCurrentTime,
                        getCalendarTime(isyaTime!!.split(":"))
                    )
                } lagi menuju sholat Isya"
            }
            else -> {
                "Mengambil data..."
            }
        }
    }

    private fun gapCounter(convertedCurrentTime: Calendar, convertedPrayerTime: Calendar): String {
        val convertedIsyaTime = getCalendarTime(isyaTime!!.split(":"))
        if (convertedCurrentTime.timeInMillis > convertedIsyaTime.timeInMillis) {
            convertedPrayerTime.add(Calendar.DATE, 1)
        }
        val currentTime = Date(convertedCurrentTime.timeInMillis)
        val prayerTime = Date(convertedPrayerTime.timeInMillis)
        val countGap = prayerTime.time - currentTime.time
        val hour = countGap / (1000 * 60 * 60)
        val minute = countGap / (1000 * 60) % 60
        return when {
            hour > 0 -> "$hour jam $minute menit"
            else -> "$minute menit"
        }
    }

    private fun getShubuhState(convertedCurrentTime: Calendar, prayerTime: String): Boolean {
        val convertedPrayerTime = getCalendarTime(prayerTime.split(":"))
        val convertedIsyaTime = getCalendarTime(isyaTime!!.split(":"))
        if (convertedCurrentTime.timeInMillis > convertedIsyaTime.timeInMillis) {
            convertedPrayerTime.add(Calendar.DATE, 1)
        }

        return convertedCurrentTime.timeInMillis < convertedPrayerTime.timeInMillis
    }

    private fun getCalendarTime(splittedTime: List<String>?): Calendar {
        val convertedTime = Calendar.getInstance()
        val currentDay = SimpleDateFormat("dd", Locale.getDefault()).format(Date()).toInt()
        splittedTime?.let {
            val sholatHour = splittedTime[0].toInt()
            val sholatMinute = splittedTime[1].toInt()
            convertedTime.set(Calendar.HOUR_OF_DAY, sholatHour)
            convertedTime.set(Calendar.MINUTE, sholatMinute)
            convertedTime.set(Calendar.SECOND, 0)
            convertedTime.set(Calendar.DAY_OF_WEEK_IN_MONTH, currentDay)
        }

        return convertedTime
    }

    private fun getCurrentState(convertedCurrentTime: Calendar, prayerTime: String): Boolean {
        val convertedPrayerTime = getCalendarTime(prayerTime.split(":"))
        return convertedPrayerTime.timeInMillis > convertedCurrentTime.timeInMillis
    }
}
