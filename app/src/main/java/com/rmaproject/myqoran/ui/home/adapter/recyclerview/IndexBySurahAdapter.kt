package com.rmaproject.myqoran.ui.home.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.Surah
import com.rmaproject.myqoran.databinding.ItemIndexSurahBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexBySurahAdapter.IndexBySurahAdapterViewHolders

class IndexBySurahAdapter(private val surahList:List<Surah>) : Adapter<IndexBySurahAdapterViewHolders>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndexBySurahAdapterViewHolders {
        return IndexBySurahAdapterViewHolders(LayoutInflater.from(parent.context).inflate(R.layout.item_index_surah, parent, false))
    }

    override fun onBindViewHolder(holder: IndexBySurahAdapterViewHolders, position: Int) {
        val surah = surahList[position]
        holder.bindView(surah)
    }

    override fun getItemCount() = surahList.size

    class IndexBySurahAdapterViewHolders(view: View) : ViewHolder(view) {
        private val binding:ItemIndexSurahBinding by viewBinding()
        fun bindView(surah:Surah) {
            binding.txtNamaSurah.text = surah.surahNameEN
            binding.txtDescendPlace.text = surah.turunSurah
            binding.txtJumlahAyat.text = "${surah.numberOfAyah.toString()} Ayat"
            binding.txtSurahNameAr.text = surah.surahNameArabic
        }
    }
}