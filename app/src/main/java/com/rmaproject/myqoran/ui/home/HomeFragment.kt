package com.rmaproject.myqoran.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.google.android.material.tabs.TabLayoutMediator
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentHomeBinding
import com.rmaproject.myqoran.ui.home.adapter.viewpager.ViewPagerAdapter
import com.rmaproject.myqoran.ui.read.ReadFragment
import com.rmaproject.myqoran.ui.settings.preferences.RecentReadPreferences
import com.rmaproject.myqoran.viewmodel.MainTabViewModel
import com.rmaproject.myqoran.viewmodel.SholatScheduleViewModel
import com.rmaproject.myqoran.viewmodel.ValuesViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: MainTabViewModel by activityViewModels()
    private val getTotalValues:ValuesViewModel by activityViewModels()
    private val sholatScheduleViewModel:SholatScheduleViewModel by activityViewModels()
    private lateinit var coroutineJob:Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        setAdapter(viewPagerAdapter)
        setButtonHeader()
        setHijriDate()

        Log.d("recentsurahnumber", RecentReadPreferences.lastReadSurahNumber.toString())
        Log.d("recentjuznumber", RecentReadPreferences.lastReadJuzNumber.toString())
        Log.d("recentpage", RecentReadPreferences.lastReadPageNumber.toString())

        Log.d("lastpos", RecentReadPreferences.lastReadPosition.toString())
        Log.d("index by", RecentReadPreferences.position.toString())
    }

    private fun setButtonHeader() {
        val recentReadSurahName = RecentReadPreferences.lastReadSurah
        val recentReadAyahNumber = RecentReadPreferences.lastReadAyah
        binding.run {
            txtRecentRead.text = "$recentReadSurahName: $recentReadAyahNumber"
            cardRecentRead.setOnClickListener {
                goToLastReadPage()
            }
            cardSholahSchedule.setOnClickListener {
                goToSchedulePage()
            }
            coroutineJob = CoroutineScope(Dispatchers.Main).launch {
                while (true) {
                    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()).split(":")
                    txtGapBetweenPrayerTimes.text = sholatScheduleViewModel.getGapBetweenTimes(currentTime)
                    delay(1000)
                }
            }
        }
    }

    private fun goToSchedulePage() {
        findNavController().navigate(R.id.action_nav_home_to_sholatScheduleFragment)
    }

    private fun goToLastReadPage() {
        val bundle:Bundle
        val isFromHome = true
        when (RecentReadPreferences.position) {
            0 -> {
                bundle = bundleOf(
                    ReadFragment.TAB_POSITION to RecentReadPreferences.position,
                    ReadFragment.SURAH_NUMBER_KEY to RecentReadPreferences.lastReadSurahNumber,
                    ReadFragment.TOTAL_INDEX to getTotalValues.totalSurahInQoran,
                    ReadFragment.IS_FROM_HOME_KEY to isFromHome
                )
                findNavController().navigate(R.id.action_nav_home_to_nav_read_fragment, bundle)
            }
            1 -> {
                bundle = bundleOf(
                    ReadFragment.TAB_POSITION to RecentReadPreferences.position,
                    ReadFragment.JUZ_NUMBER_KEY to RecentReadPreferences.lastReadJuzNumber,
                    ReadFragment.TOTAL_INDEX to getTotalValues.totalJuzInQoran,
                    ReadFragment.IS_FROM_HOME_KEY to isFromHome
                )
                findNavController().navigate(R.id.action_nav_home_to_nav_read_fragment, bundle)
            }
            2 -> {
                bundle = bundleOf(
                    ReadFragment.TAB_POSITION to RecentReadPreferences.position,
                    ReadFragment.PAGE_NUMBER_KEY to RecentReadPreferences.lastReadPageNumber,
                    ReadFragment.TOTAL_INDEX to getTotalValues.totalPageInQoran,
                    ReadFragment.IS_FROM_HOME_KEY to isFromHome
                )
                findNavController().navigate(R.id.action_nav_home_to_nav_read_fragment, bundle)
            }
        }
    }

    private fun setHijriDate() {
        val en = Locale.ENGLISH
        val namaHari = arrayListOf(
            "Senin",
            "Selasa",
            "Rabu",
            "Kamis",
            "Jum'at",
            "Sabtu",
            "Minggu",
            "Unknown Day"
        )
        val cal = UmmalquraCalendar(en)
        val tanggalHijriyyah = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
        val tanggalanHijriyyah =
            "Sekarang tanggal: " + "${namaHari[converDayToID(tanggalHijriyyah)]}, ${cal[Calendar.DAY_OF_MONTH]} ${
                cal.getDisplayName(
                    Calendar.MONTH,
                    Calendar.LONG,
                    en
                )
            } ${cal[Calendar.YEAR]}"
        binding.hijriDateTxt.text = tanggalanHijriyyah
    }

    private fun setAdapter(adapter: ViewPagerAdapter) {
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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

    private fun converDayToID(hijriDate: String?): Int {
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

    override fun onDestroyView() {
        super.onDestroyView()
        if (coroutineJob.isActive) {
            coroutineJob.cancel()
        }
    }
}