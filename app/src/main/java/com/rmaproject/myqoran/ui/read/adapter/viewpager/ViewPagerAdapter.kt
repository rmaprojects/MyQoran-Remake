package com.rmaproject.myqoran.ui.read.adapter.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.ItemPageReadQuranBinding
import com.rmaproject.myqoran.ui.footnotes.FootNotesBottomSheetFragment
import com.rmaproject.myqoran.ui.read.ReadFragment
import com.rmaproject.myqoran.ui.read.adapter.recyclerview.RecyclerViewReadQuranAdapter
import com.rmaproject.myqoran.ui.read.adapter.viewpager.ViewPagerAdapter.ViewPagerAdapterViewHolder
import com.rmaproject.myqoran.ui.settings.preferences.RecentReadPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewPagerAdapter(
    private val totalIndex: Int,
    private val indexType: Int,
    private val viewLifecycleOwner: LifecycleOwner,
    private val listTotalAyah: List<Int>,
    private val findNavController: NavController,
    private val isFromHome:Boolean,
    private val lifecycleScope: LifecycleCoroutineScope
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
            lifecycleScope
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
            lifecycleScope: LifecycleCoroutineScope
        ) {
            setAdapter(
                indexType,
                context,
                position,
                viewLifecycleOwner,
                listTotalAyah,
                findNavController,
                isFromHome,
                lifecycleScope
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
            lifecycleScope: LifecycleCoroutineScope
        ) {
            val quranDao = QuranDatabase.getInstance(context).quranDao()
            val lastReadPosition = RecentReadPreferences.lastReadPosition
            when (indexType) {
                ReadFragment.INDEX_BY_SURAH -> {
                    quranDao.readQuranBySurah(position + 1).asLiveData()
                        .observe(viewLifecycleOwner) { listQuranSurah ->
                            val adapter = RecyclerViewReadQuranAdapter(
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

                            showFootnotes(adapter, findNavController)
                        }
                }
                ReadFragment.INDEX_BY_JUZ -> {
                    quranDao.readQuranByJuz(position + 1).asLiveData()
                        .observe(viewLifecycleOwner) { listQuranJuz ->
                            val adapter = RecyclerViewReadQuranAdapter(
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

                            showFootnotes(adapter, findNavController)
                        }
                }
                ReadFragment.INDEX_BY_PAGE -> {
                    quranDao.readQuranByPage(position + 1).asLiveData()
                        .observe(viewLifecycleOwner) { listQuranPage ->
                            val adapter = RecyclerViewReadQuranAdapter(
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
    }
}