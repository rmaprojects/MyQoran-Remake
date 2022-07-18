package com.rmaproject.myqoran.ui.sholat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.api.ApiInterface
import com.rmaproject.myqoran.databinding.FragmentSholatScheduleBinding
import com.rmaproject.myqoran.viewmodel.LocationViewModel
import com.rmaproject.myqoran.viewmodel.SholatScheduleViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class SholatScheduleFragment : Fragment(R.layout.fragment_sholat_schedule) {

    private val locationViewModel:LocationViewModel by activityViewModels()
    private val sholatScheduleViewModel:SholatScheduleViewModel by activityViewModels()
    private val binding:FragmentSholatScheduleBinding by viewBinding()
    private lateinit var coroutineJob:Job
    private val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()).split(":")

    override fun onStart() {
        super.onStart()

        val longitude = locationViewModel.getLongitude()
        val latitude = locationViewModel.getLatitude()

        val api = ApiInterface.createApi()

        lifecycleScope.launch {
            try {
                val response = api.getJadwalSholat(latitude.toString(), longitude.toString())
                if (response.isSuccessful) {
                    response.body()?.let { sholatScheduleModel ->
                        sholatScheduleViewModel.shubuhTime = sholatScheduleModel.times?.get(0)?.fajr
                        sholatScheduleViewModel.dzuhurTime = sholatScheduleModel.times?.get(0)?.dhuhr
                        sholatScheduleViewModel.asharTime = sholatScheduleModel.times?.get(0)?.asr
                        sholatScheduleViewModel.maghribTime = sholatScheduleModel.times?.get(0)?.maghrib
                        sholatScheduleViewModel.isyaTime = sholatScheduleModel.times?.get(0)?.isha
                        applyBindings()
                    }
                } else {
                    sholatScheduleViewModel.shubuhTime = null
                    sholatScheduleViewModel.dzuhurTime = null
                    sholatScheduleViewModel.asharTime = null
                    sholatScheduleViewModel.maghribTime = null
                    sholatScheduleViewModel.isyaTime = null
                }
            } catch (e: Exception) {
                Log.d("Jadwal Sholat Err", e.toString())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sholatScheduleViewModel.shubuhTime != null) {
            applyBindings()
        }

        coroutineJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                val currentSystemClock = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                binding.txtCurrentClock.text = currentSystemClock.toString()
                binding.txtPredictionNextSholat.text = getGapBetweenTimes()
                delay(1000)
            }
        }
    }

    private fun applyBindings() {
        binding.apply {
            txtShubuhClock.text = sholatScheduleViewModel.shubuhTime
            txtDzuhurClock.text = sholatScheduleViewModel.dzuhurTime
            txtAsharClock.text = sholatScheduleViewModel.asharTime
            txtMaghribClock.text = sholatScheduleViewModel.maghribTime
            txtIsyaClock.text = sholatScheduleViewModel.isyaTime
        }
    }


    private fun getGapBetweenTimes() : String{
        val convertedCurrentTime = getCalendarTime(currentTime)
        val shubuhState = sholatScheduleViewModel.shubuhTime?.let { time -> getCurrentState(convertedCurrentTime, time) }
        val dzuhurState = sholatScheduleViewModel.dzuhurTime?.let { time -> getCurrentState(convertedCurrentTime, time) }
        val asharState = sholatScheduleViewModel.asharTime?.let { time -> getCurrentState(convertedCurrentTime, time) }
        val maghribState = sholatScheduleViewModel.maghribTime?.let { time -> getCurrentState(convertedCurrentTime, time) }
        val isyaState = sholatScheduleViewModel.isyaTime?.let { time -> getCurrentState(convertedCurrentTime, time) }

        Log.d("shubuhtime", getCalendarTime(sholatScheduleViewModel.shubuhTime?.split(":")).get(Calendar.HOUR_OF_DAY).toString())

        return when {
            shubuhState == true -> {
                "${gapCounter(convertedCurrentTime, getCalendarTime(sholatScheduleViewModel.shubuhTime!!.split(":")))} lagi menuju sholat Shubuh"
            }
            dzuhurState == true -> {
                "${gapCounter(convertedCurrentTime, getCalendarTime(sholatScheduleViewModel.dzuhurTime!!.split(":")))} lagi menuju sholat Dzuhur"
            }
            asharState == true -> {
                "${gapCounter(convertedCurrentTime, getCalendarTime(sholatScheduleViewModel.asharTime!!.split(":")))} lagi menuju sholat Ashar"
            }
            maghribState == true -> {
                "${gapCounter(convertedCurrentTime, getCalendarTime(sholatScheduleViewModel.maghribTime!!.split(":")))} lagi menuju sholat Maghrib"
            }
            isyaState == true -> {
                "${gapCounter(convertedCurrentTime, getCalendarTime(sholatScheduleViewModel.isyaTime!!.split(":")))} lagi menuju sholat Isya"
            }
            else -> {
                "Tidak ada sholat yang akan datang"
            }
        }
    }

    private fun gapCounter(convertedCurrentTime: Calendar, convertedPrayerTime:Calendar) : String {
        val currentTime = Date(convertedCurrentTime.timeInMillis)
        val prayerTime = Date(convertedPrayerTime.timeInMillis)
        val countGap = prayerTime.time - currentTime.time
        val hour = countGap / (1000 * 60 * 60)
        val minute = countGap / (1000 * 60) % 60
        return "$hour jam $minute menit"
    }

    private fun getCalendarTime(splittedTime:List<String>?): Calendar {
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
        return (convertedPrayerTime.timeInMillis > convertedCurrentTime.timeInMillis)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (coroutineJob.isActive) {
            coroutineJob.cancel()
        }
    }

}