package com.rmaproject.myqoran.ui.read

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayoutMediator
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentReadQuranBinding
import com.rmaproject.myqoran.ui.read.adapter.viewpager.ViewPagerAdapter
import com.rmaproject.myqoran.viewmodel.MainTabViewModel

class ReadFragment : Fragment(R.layout.fragment_read_quran) {

    private val binding: FragmentReadQuranBinding by viewBinding()
    private val viewModel:MainTabViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalIndex = arguments?.getInt(TOTAL_INDEX) ?: 0
        val surahNumber = arguments?.getInt(SURAH_NUMBER_KEY) ?: 0
        val juzNumber = arguments?.getInt(JUZ_NUMBER_KEY) ?: 0
        val pageNumber = arguments?.getInt(PAGE_NUMBER_KEY) ?: 0
        val indexType = viewModel.getTabPosition()

        setHasOptionsMenu(true)

        var jumpToPosition :Int? = 0

        when (indexType) {
            INDEX_BY_SURAH -> {
                jumpToPosition = surahNumber
                requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title = "Read Quran"
            }
            INDEX_BY_JUZ -> {
                jumpToPosition = juzNumber
                requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title = "Read Quran"
            }
            INDEX_BY_PAGE -> {
                jumpToPosition = pageNumber
                requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title = "Read Quran"
            }
        }

        val adapter = ViewPagerAdapter(totalIndex, indexType, viewLifecycleOwner)
        setViewPagerAdapter(adapter, jumpToPosition!!, indexType)
    }

    private fun setViewPagerAdapter(adapter: ViewPagerAdapter, jumpToPosition:Int, indexType :Int) {
        val quranDatabase = QuranDatabase.getInstance(requireContext())
        val quranDao = quranDatabase.quranDao()
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, index ->
            when (indexType) {
                INDEX_BY_SURAH -> {
                    quranDao.getSurahNames().asLiveData().observe(viewLifecycleOwner) { listSurah ->
                        val getSurahNames = listSurah[index]
                        tab.text = "${getSurahNames.surahName}: ${getSurahNames.total}"
                    }
                }
                INDEX_BY_JUZ -> {
                    quranDao.getJuzNames().asLiveData().observe(viewLifecycleOwner) { listJuz ->
                        val getJuzNames = listJuz[index]
                        tab.text = "Juz ${getJuzNames.jozz}"
                    }
                }
                INDEX_BY_PAGE -> {
                    quranDao.getPageNames().asLiveData().observe(viewLifecycleOwner) { listPage ->
                        val getPageNames = listPage[index]
                        tab.text = "Page ${getPageNames.page}"
                    }
                }
            }
        }.attach()
        binding.viewPager.setCurrentItem(jumpToPosition-1, true)
    }

    companion object {
        const val TOTAL_INDEX = "total_ayah"
        const val SURAH_NUMBER_KEY = "SURAH_NUMBER_KEY"
        const val JUZ_NUMBER_KEY = "JUZ_NUMBER_KEY"
        const val PAGE_NUMBER_KEY = "PAGE_NUMBER_KEY"
        const val INDEX_BY_SURAH = 0
        const val INDEX_BY_JUZ = 1
        const val INDEX_BY_PAGE = 2
    }
}