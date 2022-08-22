package com.rmaproject.myqoran.ui.read

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
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
import com.rmaproject.myqoran.helper.showMaterialAlertDialog
import com.rmaproject.myqoran.service.MyPlayerService
import com.rmaproject.myqoran.ui.read.adapter.viewpager.ViewPagerAdapter
import com.rmaproject.myqoran.viewmodel.MainTabViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import snow.player.PlayMode
import snow.player.PlayerClient

class ReadFragment : Fragment(R.layout.fragment_read_quran) {

    private val binding: FragmentReadQuranBinding by viewBinding()
    private val viewModel:MainTabViewModel by activityViewModels()
    private val playerClient by lazy {
        PlayerClient.newInstance(requireContext(), MyPlayerService::class.java)
    }
    private val menuPlay by lazy {
        binding.bottomAppbar.menu.findItem(R.id.item_play_or_pause)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalIndex = arguments?.getInt(TOTAL_INDEX) ?: 0
        val surahNumber = arguments?.getInt(SURAH_NUMBER_KEY) ?: 0
        val juzNumber = arguments?.getInt(JUZ_NUMBER_KEY) ?: 0
        val pageNumber = arguments?.getInt(PAGE_NUMBER_KEY) ?: 0
        val indexType = arguments?.getInt(TAB_POSITION)?: 0
        val isFromHome = arguments?.getBoolean(IS_FROM_HOME_KEY)?: false
        val isFromBookmark = arguments?.getBoolean(IS_FROM_BOOKMARK_KEY)?: false
        val bookmarkAyahNumber = arguments?.getInt(BOOKMARK_AYAH_NUMBER_KEY)?: 0

        var jumpToPosition = 0

        setupBottomAppBarMenu()

        binding.fabClose.isVisible = false
        binding.bottomAppbar.isVisible = false

        when (indexType) {
            INDEX_BY_SURAH -> {
                jumpToPosition = surahNumber
                requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title = getString(R.string.txt_read_by_surah)
                changeToolbarTitle(R.id.toolbar, getString(R.string.txt_read_by_surah))
            }
            INDEX_BY_JUZ -> {
                jumpToPosition = juzNumber
                changeToolbarTitle(R.id.toolbar, getString(R.string.txt_read_by_jozz))
            }
            INDEX_BY_PAGE -> {
                jumpToPosition = pageNumber
                changeToolbarTitle(R.id.toolbar, getString(R.string.txt_read_by_page))
            }
        }

        val menuHost:MenuHost = requireActivity()

        createContextMenu(menuHost)

        viewModel.getTotalAyahList().observe(viewLifecycleOwner){ listTotalAyah ->
            setViewPagerAdapter(jumpToPosition, indexType,  totalIndex, listTotalAyah, isFromHome, isFromBookmark, bookmarkAyahNumber)
        }
    }

    private fun createContextMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object:MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search_header, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.item_search -> {
                        findNavController().navigate(R.id.action_nav_read_fragment_to_searchAyahFragment)
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupBottomAppBarMenu() {

        binding.bottomAppbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_play_mode -> {
                    when (playerClient.playMode) {
                        PlayMode.SINGLE_ONCE -> {
                            playerClient.playMode = PlayMode.PLAYLIST_LOOP
                            menuItem.setIcon(R.drawable.ic_round_repeat_colored_24)
                        }
                        PlayMode.PLAYLIST_LOOP -> {
                            playerClient.playMode = PlayMode.SINGLE_ONCE
                            menuItem.setIcon(R.drawable.ic_round_repeat_24)
                        }
                        else -> {
                            playerClient.playMode = PlayMode.SINGLE_ONCE
                        }
                    }
                }
                R.id.item_next_ayah -> {
                    playerClient.skipToNext()
                }
                R.id.item_play_or_pause -> {
                    when (menuPlay.title) {
                        "Play Ayah" -> {
                            menuPlay.title = "Pause Ayah"
                            menuPlay.setIcon(R.drawable.ic_baseline_pause_24)
                            playerClient.play()
                        }
                        "Pause Ayah" -> {
                            menuPlay.title = "Play Ayah"
                            menuPlay.setIcon(R.drawable.ic_baseline_play_arrow_24)
                            playerClient.pause()
                        }
                    }
                }
                R.id.item_prev_ayah -> {
                    playerClient.skipToPrevious()
                }
                R.id.item_stop -> {
                    playerClient.stop()
                    menuPlay.title = "Play Ayah"
                    menuPlay.setIcon(R.drawable.ic_baseline_play_arrow_24)
                }
            }
            true
        }

        binding.fabClose.setOnClickListener {
            playerClient.stop()
            binding.bottomAppbar.isVisible = false
            binding.fabClose.isVisible = false
            playerClient.shutdown()
        }

        if (playerClient.isError) {
            showMaterialAlertDialog(
                requireContext(),
                getString(R.string.txt_error_occurred),
                playerClient.errorMessage,
                "Ok"
            ) {}
        }
    }

    private fun setViewPagerAdapter(
        jumpToPosition: Int,
        indexType: Int,
        totalIndex: Int,
        listTotalAyah: List<Int>,
        isFromHome: Boolean,
        isFromBookmark: Boolean,
        bookmarkAyahNumber: Int
    ) {
        val quranDatabase = QuranDatabase.getInstance(requireContext())
        val quranDao = quranDatabase.quranDao()
        val adapter = ViewPagerAdapter(totalIndex, indexType, viewLifecycleOwner, listTotalAyah, findNavController(), isFromHome, lifecycleScope, isFromBookmark, bookmarkAyahNumber, playerClient, binding)
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
        const val IS_FROM_BOOKMARK_KEY = "ISFROMBOOKMARKVALUE"
        const val BOOKMARK_AYAH_NUMBER_KEY = "BOOKMARKAYAHNUMBERVALUE"
    }
}