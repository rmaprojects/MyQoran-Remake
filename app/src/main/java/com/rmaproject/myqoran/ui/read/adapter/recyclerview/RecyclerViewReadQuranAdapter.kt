package com.rmaproject.myqoran.ui.read.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.Quran
import com.rmaproject.myqoran.databinding.ItemReadQuranBinding
import com.rmaproject.myqoran.helper.SnackbarHelper
import com.rmaproject.myqoran.ui.read.adapter.recyclerview.RecyclerViewReadQuranAdapter.RecyclerViewReadQuranAdapterViewHolder

class RecyclerViewReadQuranAdapter(private val listQuran: List<Quran>) : Adapter<RecyclerViewReadQuranAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewReadQuranAdapterViewHolder {
        return RecyclerViewReadQuranAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_read_quran, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewReadQuranAdapterViewHolder, position: Int) {
        val quran = listQuran[position]
        val totalAyah = listQuran.size
        holder.bindView(quran, position, totalAyah)
    }

    override fun getItemCount() = listQuran.size

    class RecyclerViewReadQuranAdapterViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val binding:ItemReadQuranBinding by viewBinding()
        fun bindView(quran: Quran, position: Int, totalAyah: Int) {
            setTextViewValues(quran, position, totalAyah)
            setViewClickListener()
        }

        private fun setTextViewValues(quran: Quran, position: Int, totalAyah: Int) {
            binding.headerSurahName.isVisible = position == 0
            binding.txtSurahNameEn.text = quran.surahNameEn
            binding.txtDescendPlace.text = quran.turunSurah
            binding.txtAyah.text = quran.ayahText
            binding.txtSurahNameAr.text = quran.surahNameAr
            binding.txtTotalAyah.text = "$totalAyah Ayah"
            binding.txtTranslate.text = quran.translation_id
        }

        private fun setViewClickListener() {
            binding.btnPlayAllAyah.setOnClickListener {
                SnackbarHelper.showSnackbarShort(binding.root, "Play All Ayah")
            }
            binding.txtAyah.setOnLongClickListener {
                SnackbarHelper.showSnackbarShort(binding.root, "Long Click Ayah")
                true
            }
        }
    }
}