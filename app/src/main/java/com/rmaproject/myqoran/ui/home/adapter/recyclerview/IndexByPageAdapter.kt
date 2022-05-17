package com.rmaproject.myqoran.ui.home.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.Page
import com.rmaproject.myqoran.databinding.ItemIndexPageBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexByPageAdapter.IndexByPageAdapterViewHolder

class IndexByPageAdapter(private val pageList:List<Page>) : Adapter<IndexByPageAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndexByPageAdapterViewHolder {
        return IndexByPageAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_index_page, parent, false))
    }

    override fun onBindViewHolder(holder: IndexByPageAdapterViewHolder, position: Int) {
        val pages = pageList[position]
        holder.bindView(pages)
    }

    override fun getItemCount() = pageList.size

    class IndexByPageAdapterViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        private val binding:ItemIndexPageBinding by viewBinding()
        fun bindView(pages:Page) {
            binding.txtPageIndicator.text = "Page ${pages.page}"
        }
    }
}