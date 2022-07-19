package com.rmaproject.myqoran.ui.read

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayoutMediator
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentReadQuranBinding
import com.rmaproject.myqoran.helper.changeToolbarTitle
import com.rmaproject.myqoran.ui.read.adapter.viewpager.ViewPagerAdapter
import com.rmaproject.myqoran.viewmodel.MainTabViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReadFragment : Fragment(R.layout.fragment_read_quran) {

    private val binding: FragmentReadQuranBinding by viewBinding()
    private val viewModel:MainTabViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalIndex = arguments?.getInt(TOTAL_INDEX) ?: 0
        val surahNumber = arguments?.getInt(SURAH_NUMBER_KEY) ?: 0
        val juzNumber = arguments?.getInt(JUZ_NUMBER_KEY) ?: 0
        val pageNumber = arguments?.getInt(PAGE_NUMBER_KEY) ?: 0
        val indexType = arguments?.getInt(TAB_POSITION)?: 0
        val isFromHome = arguments?.getBoolean(IS_FROM_HOME_KEY)?:false

        var jumpToPosition :Int? = 0

        when (indexType) {
            INDEX_BY_SURAH -> {
                jumpToPosition = surahNumber
                requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title = "Read by Surah"
                changeToolbarTitle(R.id.toolbar, "Read by Surah")
            }
            INDEX_BY_JUZ -> {
                jumpToPosition = juzNumber
                changeToolbarTitle(R.id.toolbar, "Read by Juz")
            }
            INDEX_BY_PAGE -> {
                jumpToPosition = pageNumber
                changeToolbarTitle(R.id.toolbar, "Read by Page")
            }
        }

        viewModel.getTotalAyahList().observe(viewLifecycleOwner){ listTotalAyah ->
            setViewPagerAdapter(jumpToPosition!!, indexType,  totalIndex, listTotalAyah, isFromHome)
        }
    }

    private fun setViewPagerAdapter(
        jumpToPosition: Int,
        indexType: Int,
        totalIndex: Int,
        listTotalAyah: List<Int>,
        isFromHome:Boolean
    ) {
        val quranDatabase = QuranDatabase.getInstance(requireContext())
        val quranDao = quranDatabase.quranDao()
        val adapter = ViewPagerAdapter(totalIndex, indexType, viewLifecycleOwner, listTotalAyah, findNavController(), isFromHome, lifecycleScope)
        binding.viewPager.adapter = adapter
        binding.viewPager.isSaveEnabled = false
        TabLayoutMediator (binding.tabLayout, binding.viewPager) { tab, index ->
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

        lifecycleScope.launch {
            delay(300)
            binding.viewPager.currentItem = jumpToPosition-1
        }
    }

    companion object {
        const val TOTAL_INDEX = "total_ayah"
        const val SURAH_NUMBER_KEY = "SURAH_NUMBER_KEY"
        const val JUZ_NUMBER_KEY = "JUZ_NUMBER_KEY"
        const val PAGE_NUMBER_KEY = "PAGE_NUMBER_KEY"
        const val INDEX_BY_SURAH = 0
        const val INDEX_BY_JUZ = 1
        const val INDEX_BY_PAGE = 2
        const val TAB_POSITION = "TABPOS"
        const val IS_FROM_HOME_KEY = "ISFROMHOMEVALUE"
    }
}