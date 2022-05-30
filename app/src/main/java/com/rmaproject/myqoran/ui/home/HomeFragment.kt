package com.rmaproject.myqoran.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.google.android.material.tabs.TabLayoutMediator
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentHomeBinding
import com.rmaproject.myqoran.ui.home.adapter.viewpager.ViewPagerAdapter
import com.rmaproject.myqoran.viewmodel.MainTabViewModel
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel:MainTabViewModel by viewModels()
    private val homeViewModel:HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        setAdapter(viewPagerAdapter)
        setFab()
        setHijriDate()
    }

    private fun setHijriDate() {
        val en = Locale.ENGLISH
        val namaHari = arrayListOf("Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu", "Unknown Day")
        val cal = UmmalquraCalendar(en)
        val tanggalHijriyyah = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
        val tanggalanHijriyyah = "${namaHari[converDayToID(tanggalHijriyyah)]}, ${cal[Calendar.DAY_OF_MONTH]} ${cal.getDisplayName(Calendar.MONTH, Calendar.LONG, en)} ${cal[Calendar.YEAR]}"
        binding.hijriDateTxt.text = "$tanggalanHijriyyah"
    }

    private fun setFab() {
        homeViewModel.hideFabListener = {
            binding.fab.hide()
        }
        homeViewModel.showFabListener = {
            binding.fab.show()
        }
        binding.fab.setOnClickListener {
            homeViewModel.goToTopListener?.invoke()
        }
    }

    private fun setAdapter(adapter: ViewPagerAdapter) {
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setTabPosition(position)
                Log.d("page selected:", viewModel.getTabPosition().toString())
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Surah"
                1 -> "Juz"
                2 -> "Page"
                else -> "Unknown"
            }
        }.attach()
    }

    private fun converDayToID(hijriDate:String?): Int {
        return when (hijriDate) {
            "Sunday" -> {
                6
            }
            "Monday" -> {
                0
            }
            "Tuesday" -> {
                1
            }
            "Wednesday" -> {
                2
            }
            "Thursday" -> {
                3
            }
            "Friday" -> {
                4
            }
            "Saturday" -> {
                5
            }
            else -> {
                7
            }
        }
    }
}