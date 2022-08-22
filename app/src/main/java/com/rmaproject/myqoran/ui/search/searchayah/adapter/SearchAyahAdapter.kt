package com.rmaproject.myqoran.ui.search.searchayah.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.Quran
import com.rmaproject.myqoran.databinding.ItemSearchAyahBinding
import com.rmaproject.myqoran.ui.search.searchayah.adapter.SearchAyahAdapter.SearchAyahAdapterViewHolder
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences

class SearchAyahAdapter(private val listSearchAyah:List<Quran>) : RecyclerView.Adapter<SearchAyahAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchAyahAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_ayah, parent, false))

    override fun onBindViewHolder(holder: SearchAyahAdapterViewHolder, position: Int) {
        val quran = listSearchAyah[position]
        holder.bindView(quran)
    }

    override fun getItemCount() = listSearchAyah.size

    class SearchAyahAdapterViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val binding:ItemSearchAyahBinding by viewBinding()
        fun bindView(quran: Quran) {
            with(binding) {
                txtAyah.text = quran.ayahText
                txtTranslate.text = if (SettingsPreferences.languagePreference == 0) {
                    quran.translation_id
                } else {
                    quran.translation_en
                }
                txtSurahName.text = "Q.S ${quran.surahNameEn}:${quran.ayahNumber}"
            }
        }
    }
}