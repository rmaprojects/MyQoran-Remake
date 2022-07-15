package com.rmaproject.myqoran.ui.home.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.l4digital.fastscroll.FastScroller
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.LastAyahFinderJuz
import com.rmaproject.myqoran.database.model.Juz
import com.rmaproject.myqoran.databinding.ItemIndexJuzBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexByJuzAdapter.IndexByJuzAdapterViewHolder

class IndexByJuzAdapter(private val juzList:List<Juz>, private val lastSurahOfJuzList:List<LastAyahFinderJuz>) : Adapter<IndexByJuzAdapterViewHolder>(), FastScroller.SectionIndexer {

    var listener:((Juz, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexByJuzAdapterViewHolder {
        return IndexByJuzAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_index_juz, parent, false))
    }

    override fun onBindViewHolder(holder: IndexByJuzAdapterViewHolder, position: Int) {
        val juz = juzList[position]
        val lastAyah = lastSurahOfJuzList[position]
        holder.binding.clickableCardView.setOnClickListener {
            listener?.invoke(juz, juzList.size)
        }
        holder.bindView(juz, lastAyah)
    }

    override fun getItemCount() = juzList.size

    class IndexByJuzAdapterViewHolder(itemView: View) : ViewHolder(itemView) {
        val binding: ItemIndexJuzBinding by viewBinding()
        fun bindView(juz: Juz, lastAyah: LastAyahFinderJuz) {
            binding.txtJuzNumber.text = "Juz ${juz.juzNumber}"
            binding.txtFirstSurahInJuz.text = "Surah ${juz.SurahName_en} ${juz.nomorAyah}"
            binding.txtLastSurahInJuz.text = "${lastAyah.SurahName_en} ${lastAyah.ayahNumber}"
        }
    }

    override fun getSectionText(position: Int): CharSequence {
        return "Juz ${juzList[position].juzNumber}"
    }
}