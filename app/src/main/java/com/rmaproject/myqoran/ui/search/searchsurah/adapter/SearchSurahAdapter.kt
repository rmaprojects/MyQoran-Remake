package com.rmaproject.myqoran.ui.search.searchsurah.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.SearchSurahResult
import com.rmaproject.myqoran.databinding.ItemSearchSurahBinding
import com.rmaproject.myqoran.ui.search.searchsurah.adapter.SearchSurahAdapter.SearchSurahAdapterViewHolder

class SearchSurahAdapter(private val searchResult:List<SearchSurahResult>) : RecyclerView.Adapter<SearchSurahAdapterViewHolder>() {

    var clickSurahItemListener:((SearchSurahResult) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchSurahAdapterViewHolder {
        return SearchSurahAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_surah, parent, false))
    }

    override fun onBindViewHolder(holder: SearchSurahAdapterViewHolder, position: Int) {
        val searchItemResult = searchResult[position]
        holder.bindView(searchItemResult)
        holder.binding.run {
            clickableCardView.setOnClickListener {
                clickSurahItemListener?.invoke(searchItemResult)
            }
        }
    }

    override fun getItemCount() = searchResult.size

    class SearchSurahAdapterViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val binding:ItemSearchSurahBinding by viewBinding()
        fun bindView(searchItemResult: SearchSurahResult) {
            binding.run {
                txtSurahNameEn.text = searchItemResult.surahNameEN
                txtDescendPlace.text = searchItemResult.turunSurah
                txtSurahNameAr.text = searchItemResult.surahNameArabic
                txtJumlahAyat.text = searchItemResult.numberOfAyah.toString()
            }
        }
    }
}