package com.rmaproject.myqoran.ui.sholat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
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

    override fun onStart() {
        super.onStart()

        val longitude = locationViewModel.getLongitude()
        val latitude = locationViewModel.getLatitude()

        val api = ApiInterface.createApi()

        lifecycleScope.launch {
            try {
                val response = api.getJadwalSholat(latitude.toString(), longitude.toString())
                if (response.isSuccessful) {
                    sholatScheduleViewModel.shubuhTime = response.body()?.times?.get(0)?.fajr
                    sholatScheduleViewModel.dzuhurTime = response.body()?.times?.get(0)?.dhuhr
                    sholatScheduleViewModel.asharTime = response.body()?.times?.get(0)?.asr
                    sholatScheduleViewModel.maghribTime = response.body()?.times?.get(0)?.maghrib
                    sholatScheduleViewModel.isyaTime = response.body()?.times?.get(0)?.isha
                } else {
                    sholatScheduleViewModel.shubuhTime = null
                    sholatScheduleViewModel.dzuhurTime = null
                    sholatScheduleViewModel.asharTime = null
                    sholatScheduleViewModel.maghribTime = null
                    sholatScheduleViewModel.isyaTime = null
                }
            } catch (e: Exception) {
                Log.d("Jadwal Sholat Err", e.toString())
                Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data, periksa kembali internet anda", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sholatScheduleViewModel.shubuhTime != null) {
            binding.txtShubuhClock.text = sholatScheduleViewModel.shubuhTime
            binding.txtDzuhurClock.text = sholatScheduleViewModel.dzuhurTime
            binding.txtAsharClock.text = sholatScheduleViewModel.asharTime
            binding.txtMaghribClock.text = sholatScheduleViewModel.maghribTime
            binding.txtIsyaClock.text = sholatScheduleViewModel.isyaTime
        }

        coroutineJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                val currentSystemClock = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                binding.txtCurrentClock.text = currentSystemClock.toString()
                binding.txtPredictionNextSholat.text = sholatScheduleViewModel.getGapBetweenTimes()
                delay(1000)
            }
        }

        Log.d("current time", SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineJob.cancel()
    }

}