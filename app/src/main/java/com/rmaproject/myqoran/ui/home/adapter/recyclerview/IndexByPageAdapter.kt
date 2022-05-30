package com.rmaproject.myqoran.ui.home.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.l4digital.fastscroll.FastScroller
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.LastAyahFinderPage
import com.rmaproject.myqoran.database.model.Page
import com.rmaproject.myqoran.databinding.ItemIndexPageBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexByPageAdapter.IndexByPageAdapterViewHolder

class IndexByPageAdapter(private val pageList:List<Page>, private val lastAyahList:List<LastAyahFinderPage>) : Adapter<IndexByPageAdapterViewHolder>(), FastScroller.SectionIndexer {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndexByPageAdapterViewHolder {
        return IndexByPageAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_index_page, parent, false))
    }

    override fun onBindViewHolder(holder: IndexByPageAdapterViewHolder, position: Int) {
        val pages = pageList[position]
        val lastAyah = lastAyahList[position]
        holder.bindView(pages, lastAyah)
    }

    override fun getItemCount() = pageList.size

    class IndexByPageAdapterViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        private val binding:ItemIndexPageBinding by viewBinding()
        fun bindView(pages: Page, lastAyah: LastAyahFinderPage) {
            binding.txtPageIndicator.text = "Page ${pages.page}"
            binding.txtFirstAyahInPage.text = "Surah ${pages.SurahName_en} ${pages.AyahNumber}"
            binding.txtLastAyahInPage.text = "${lastAyah.SurahName_en} ${lastAyah.AyahNumber}"
        }
    }

    override fun getSectionText(position: Int): CharSequence {
        return "Page ${pageList[position].page}"
    }
}