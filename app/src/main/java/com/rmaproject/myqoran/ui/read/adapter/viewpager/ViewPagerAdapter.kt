package com.rmaproject.myqoran.ui.read.adapter.viewpager

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.BookmarkDatabase
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.database.model.Quran
import com.rmaproject.myqoran.databinding.FragmentReadQuranBinding
import com.rmaproject.myqoran.databinding.ItemPageReadQuranBinding
import com.rmaproject.myqoran.ui.footnotes.FootNotesBottomSheetFragment
import com.rmaproject.myqoran.ui.read.ReadFragment
import com.rmaproject.myqoran.ui.read.adapter.recyclerview.RecyclerViewReadQuranAdapter
import com.rmaproject.myqoran.ui.read.adapter.viewpager.ViewPagerAdapter.ViewPagerAdapterViewHolder
import com.rmaproject.myqoran.ui.settings.preferences.RecentReadPreferences
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import snow.player.PlayMode
import snow.player.PlayerClient
import snow.player.audio.MusicItem
import snow.player.playlist.Playlist

class ViewPagerAdapter(
    private val totalIndex: Int,
    private val indexType: Int,
    private val viewLifecycleOwner: LifecycleOwner,
    private val listTotalAyah: List<Int>,
    private val findNavController: NavController,
    private val isFromHome: Boolean,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val isFromBookmark: Boolean,
    private val bookmarkAyahNumber: Int,
    private val playerClient: PlayerClient,
    private val parentBinding: FragmentReadQuranBinding
) : Adapter<ViewPagerAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapterViewHolder {
        return ViewPagerAdapterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_page_read_quran, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewPagerAdapterViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bindView(
            indexType,
            context,
            position,
            viewLifecycleOwner,
            listTotalAyah,
            findNavController,
            isFromHome,
            lifecycleScope,
            isFromBookmark,
            bookmarkAyahNumber,
            playerClient,
            parentBinding
        )
    }

    override fun getItemCount() = totalIndex

    class ViewPagerAdapterViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding: ItemPageReadQuranBinding by viewBinding()
        fun bindView(
            indexType: Int,
            context: Context,
            position: Int,
            viewLifecycleOwner: LifecycleOwner,
            listTotalAyah: List<Int>,
            findNavController: NavController,
            isFromHome: Boolean,
            lifecycleScope: LifecycleCoroutineScope,
            isFromBookmark: Boolean,
            bookmarkAyahNumber: Int,
            playerClient: PlayerClient,
            parentBinding: FragmentReadQuranBinding
        ) {
            setAdapter(
                indexType,
                context,
                position,
                viewLifecycleOwner,
                listTotalAyah,
                findNavController,
                isFromHome,
                lifecycleScope,
                isFromBookmark,
                bookmarkAyahNumber,
                playerClient,
                parentBinding
            )
        }

        private fun setAdapter(
            indexType: Int,
            context: Context,
            position: Int,
            viewLifecycleOwner: LifecycleOwner,
            listTotalAyah: List<Int>,
            findNavController: NavController,
            isFromHome: Boolean,
            lifecycleScope: LifecycleCoroutineScope,
            isFromBookmark: Boolean,
            bookmarkAyahNumber: Int,
            playerClient: PlayerClient,
            parentBinding: FragmentReadQuranBinding
        ) {
            val quranDao = QuranDatabase.getInstance(context).quranDao()
            val lastReadPosition = RecentReadPreferences.lastReadPosition
            var adapter: RecyclerViewReadQuranAdapter
            when (indexType) {
                ReadFragment.INDEX_BY_SURAH -> {
                    quranDao.readQuranBySurah(position + 1).asLiveData()
                        .observe(viewLifecycleOwner) { listQuranSurah ->
                            adapter = RecyclerViewReadQuranAdapter(
                                listQuranSurah,
                                listTotalAyah,
                                indexType,
                            )
                            binding.recyclerView.adapter = adapter
                            binding.recyclerView.layoutManager = LinearLayoutManager(context)

                            if (isFromHome) {
                                lifecycleScope.launch {
                                    delay(1000)
                                    binding.recyclerView.scrollToPosition(lastReadPosition)
                                }
                            }

                            if (isFromBookmark) {
                                lifecycleScope.launch {
                                    delay(1000)
                                    binding.recyclerView.scrollToPosition(bookmarkAyahNumber - 1)
                                }
                            }

                            setupBookmarkOnClick(adapter, context, lifecycleScope)
                            setupPlayAyahOnClick(adapter, playerClient, parentBinding)
                            setupPlayAllAyahOnClick(adapter, playerClient, parentBinding)
                            showFootnotes(adapter, findNavController)
                        }
                }
                ReadFragment.INDEX_BY_JUZ -> {
                    quranDao.readQuranByJuz(position + 1).asLiveData()
                        .observe(viewLifecycleOwner) { listQuranJuz ->
                            adapter = RecyclerViewReadQuranAdapter(
                                listQuranJuz,
                                listTotalAyah,
                                indexType,
                            )
                            binding.recyclerView.adapter = adapter
                            binding.recyclerView.layoutManager = LinearLayoutManager(context)

                            if (isFromHome) {
                                lifecycleScope.launch {
                                    delay(1000)
                                    binding.recyclerView.scrollToPosition(lastReadPosition)
                                }
                            }

                            setupBookmarkOnClick(adapter, context, lifecycleScope)
                            setupPlayAyahOnClick(adapter, playerClient, parentBinding)
                            showFootnotes(adapter, findNavController)
                        }
                }
                ReadFragment.INDEX_BY_PAGE -> {
                    quranDao.readQuranByPage(position + 1).asLiveData()
                        .observe(viewLifecycleOwner) { listQuranPage ->
                            adapter = RecyclerViewReadQuranAdapter(
                                listQuranPage,
                                listTotalAyah,
                                indexType,
                            )
                            binding.recyclerView.adapter = adapter
                            binding.recyclerView.layoutManager = LinearLayoutManager(context)

                            if (isFromHome) {
                                lifecycleScope.launch {
                                    delay(300)
                                    binding.recyclerView.scrollToPosition(lastReadPosition)
                                }
                            }

                            setupBookmarkOnClick(adapter, context, lifecycleScope)
                            setupPlayAyahOnClick(adapter, playerClient, parentBinding)
                            showFootnotes(adapter, findNavController)
                        }
                }
            }
        }

        private fun showFootnotes(
            adapter: RecyclerViewReadQuranAdapter,
            findNavController: NavController
        ) {
            adapter.footNoteonClickListener = { footnote ->
                val bundle = bundleOf(FootNotesBottomSheetFragment.RECEIVE_FOOTNOTE_KEY to footnote)
                findNavController.navigate(
                    R.id.action_nav_read_fragment_to_nav_bottom_sheet_footnotes,
                    bundle
                )
            }
        }

        private fun setupBookmarkOnClick(
            adapter: RecyclerViewReadQuranAdapter,
            context: Context,
            lifecycleScope: LifecycleCoroutineScope
        ) {
            adapter.addBookmarkClickListener = { bookmark ->
                val database = BookmarkDatabase.getInstance(context)
                val dao = database.bookmarkDao()
                lifecycleScope.launch {
                    dao.insertBookmark(bookmark)
                }
            }
        }

        private fun setupPlayAyahOnClick(
            adapter: RecyclerViewReadQuranAdapter,
            playerClient: PlayerClient,
            parentBinding: FragmentReadQuranBinding
        ) {
            adapter.playAyahOnClickListener = { listQuran, position ->
                playerClient.connect { isConnected ->
                    Log.d("CONNECTED?", isConnected.toString())
                    if (isConnected) {
                        parentBinding.fabClose.isVisible = true
                        parentBinding.bottomAppbar.isVisible = true
                        if (playerClient.isPlaying) {
                            playerClient.stop()
                        }
                        playerClient.setPlaylist(createPlayAllPlayList(listQuran), position, true)
                        playerClient.playMode = PlayMode.SINGLE_ONCE
                        parentBinding.bottomAppbar.menu.findItem(R.id.item_play_mode)
                            .setIcon(R.drawable.ic_round_repeat_24)
                        playerClient.addOnPlayingMusicItemChangeListener { _, position, _->
                            binding.recyclerView.smoothScrollToPosition(position)
                        }
                    }
                }
            }
        }

        private fun setupPlayAllAyahOnClick(
            adapter: RecyclerViewReadQuranAdapter,
            playerClient: PlayerClient,
            parentBinding: FragmentReadQuranBinding
        ) {
            adapter.playAllAyahOnClickListener = { listQuran, _ ->
                playerClient.connect { isConnected ->
                    if (isConnected) {
                        val playlist = createPlayAllPlayList(listQuran)
                        parentBinding.fabClose.isVisible = true
                        parentBinding.bottomAppbar.isVisible = true
                        if (playerClient.isPlaying) {
                            playerClient.stop()
                        }
                        playerClient.setPlaylist(playlist, true)
                        playerClient.playMode = PlayMode.PLAYLIST_LOOP
                        playerClient.addOnPlayingMusicItemChangeListener { _, position, _ ->
                            binding.recyclerView.smoothScrollToPosition(position)
                        }
                        parentBinding.bottomAppbar.menu.findItem(R.id.item_play_mode)
                            .setIcon(R.drawable.ic_round_repeat_colored_24)
                    }
                }
            }
        }

        private fun createPlayAllPlayList(listQuran: List<Quran>): Playlist {
            val ayahItemList = mutableListOf<MusicItem>()
            listQuran.forEach { quran ->
                val formattedAyahNumber = formatNumber(quran.ayahNumber)
                val formattedSurahNumber = formatNumber(quran.surahNumber)
                val playList = MusicItem.Builder()
                    .setTitle("${quran.surahNameEn}: ${quran.ayahNumber}")
                    .autoDuration()
                    .setArtist(getReciterName())
                    .setUri("https://www.everyayah.com/data/${getReciterId()}/${formattedSurahNumber}${formattedAyahNumber}.mp3")
                    .setIconUri("https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/10/04/676590316.jpg")
                    .setMusicId("${quran.surahNumber}:${quran.ayahNumber}")
                    .build()
                ayahItemList.add(playList)
            }
            return Playlist.Builder()
                .appendAll(ayahItemList)
                .build()
        }

        private fun getReciterId(): String {
            return when (SettingsPreferences.currentReciter) {
                0 -> "Abdurrahmaan_As-Sudais_64kbps"
                1 -> "Alafasy_64kbps"
                2 -> "Hudhaify_64kbps"
                3 -> "Muhammad_Ayyoub_64kbps"
                else -> "Abdurrahmaan_As-Sudais_64kbps"
            }
        }

        private fun getReciterName(): String {
            return when (SettingsPreferences.currentReciter) {
                0 -> "Abdurrahman As Sudais"
                1 -> "Alafasy"
                2 -> "Hudaify"
                3 -> "Muhammad Ayyoub"
                else -> "Abdurrahman As Sudais"
            }
        }

        private fun formatNumber(numberToBeFormatted: Int?): String {
            return String.format("%03d", numberToBeFormatted)
        }
    }
}