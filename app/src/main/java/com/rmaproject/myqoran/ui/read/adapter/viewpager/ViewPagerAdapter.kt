package com.rmaproject.myqoran.ui.read.adapter.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.ItemPageReadQuranBinding
import com.rmaproject.myqoran.ui.read.ReadFragment
import com.rmaproject.myqoran.ui.read.adapter.recyclerview.RecyclerViewReadQuranAdapter
import com.rmaproject.myqoran.ui.read.adapter.viewpager.ViewPagerAdapter.ViewPagerAdapterViewHolder

class ViewPagerAdapter(
    private val totalIndex: Int,
    private val indexType: Int,
    private val viewLifecycleOwner: LifecycleOwner
) : Adapter<ViewPagerAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapterViewHolder {
        return ViewPagerAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page_read_quran, parent, false))
    }

    override fun onBindViewHolder(holder: ViewPagerAdapterViewHolder, position: Int) {
        holder.bindView(indexType, holder.itemView.context, position, viewLifecycleOwner)
    }

    override fun getItemCount() = totalIndex

    class ViewPagerAdapterViewHolder(itemView:View) : ViewHolder(itemView) {
        private val binding:ItemPageReadQuranBinding by viewBinding()
        fun bindView(
            indexType: Int,
            context: Context,
            position: Int,
            viewLifecycleOwner: LifecycleOwner
        ) {
            setAdapter(indexType, context, position, viewLifecycleOwner)
        }

        private fun setAdapter(
            indexType: Int,
            context: Context,
            position: Int,
            viewLifecycleOwner: LifecycleOwner
        ) {
            val quranDao = QuranDatabase.getInstance(context).quranDao()
            when (indexType) {
                ReadFragment.INDEX_BY_SURAH -> {
                    quranDao.readQuranBySurah(position+1).asLiveData().observe(viewLifecycleOwner) { listQuranSurah ->
                        val adapter = RecyclerViewReadQuranAdapter(listQuranSurah)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    }
                }
                ReadFragment.INDEX_BY_JUZ -> {
                    quranDao.readQuranByJuz(position+1).asLiveData().observe(viewLifecycleOwner) { listQuranJuz ->
                        val adapter = RecyclerViewReadQuranAdapter(listQuranJuz)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    }
                }
                ReadFragment.INDEX_BY_PAGE -> {
                    quranDao.readQuranByPage(position+1).asLiveData().observe(viewLifecycleOwner) { listQuranPage ->
                        val adapter = RecyclerViewReadQuranAdapter(listQuranPage)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        }
    }
}