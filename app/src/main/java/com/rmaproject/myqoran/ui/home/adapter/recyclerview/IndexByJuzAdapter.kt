package com.rmaproject.myqoran.ui.home.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.Juz
import com.rmaproject.myqoran.databinding.ItemIndexJuzBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexByJuzAdapter.IndexByJuzAdapterViewHolder

class IndexByJuzAdapter(private val juzList:List<Juz>) : Adapter<IndexByJuzAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexByJuzAdapterViewHolder {
        return IndexByJuzAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_index_juz, parent, false))
    }

    override fun onBindViewHolder(holder: IndexByJuzAdapterViewHolder, position: Int) {
        val juz = juzList[position]
        holder.bindView(juz)
    }

    override fun getItemCount() = juzList.size

    class IndexByJuzAdapterViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding: ItemIndexJuzBinding by viewBinding()
        fun bindView(juz:Juz) {
            binding.txtJuzNumber.text = "Juz ${juz.juzNumber}"
        }
    }
}