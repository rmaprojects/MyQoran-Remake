package com.rmaproject.myqoran.ui.sholat

import android.location.Geocoder
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
                binding.txtPredictionNextSholat.text = sholatScheduleViewModel.getGapBetweenTimes(currentSystemClock.split(":"))
                delay(1000)
            }
        }
    }

    private fun applyBindings() {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault()).getFromLocation(locationViewModel.getLatitude(), locationViewModel.getLongitude(), 1)
        val locality = geoCoder[0].locality
        val subLocality = geoCoder[0].subLocality
        val subAdminArea = geoCoder[0].subAdminArea
        val address = "$locality, $subLocality, $subAdminArea"
        binding.apply {
            txtShubuhClock.text = sholatScheduleViewModel.shubuhTime
            txtDzuhurClock.text = sholatScheduleViewModel.dzuhurTime
            txtAsharClock.text = sholatScheduleViewModel.asharTime
            txtMaghribClock.text = sholatScheduleViewModel.maghribTime
            txtIsyaClock.text = sholatScheduleViewModel.isyaTime
            txtCurrentLocation.text = address
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (coroutineJob.isActive) {
            coroutineJob.cancel()
        }
    }

}