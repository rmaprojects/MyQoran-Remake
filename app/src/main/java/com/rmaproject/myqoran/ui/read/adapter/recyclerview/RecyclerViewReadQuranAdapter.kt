package com.rmaproject.myqoran.ui.read.adapter.recyclerview

import android.content.Context
import android.text.Spannable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.Quran
import com.rmaproject.myqoran.databinding.ItemReadQuranBinding
import com.rmaproject.myqoran.helper.SnackbarHelper
import com.rmaproject.myqoran.helper.TajweedHelper
import com.rmaproject.myqoran.ui.read.adapter.recyclerview.RecyclerViewReadQuranAdapter.RecyclerViewReadQuranAdapterViewHolder

class RecyclerViewReadQuranAdapter(
    private val listQuran: List<Quran>,
    private val listTotalAyah: List<Int>
) : Adapter<RecyclerViewReadQuranAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewReadQuranAdapterViewHolder {
        return RecyclerViewReadQuranAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_read_quran, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewReadQuranAdapterViewHolder, position: Int) {
        val quran = listQuran[position]
        val totalAyah = listTotalAyah[quran.surahNumber!! - 1]
        val context = holder.itemView.context
        holder.bindView(quran, totalAyah, context)
    }

    override fun getItemCount() = listQuran.size

    class RecyclerViewReadQuranAdapterViewHolder(view:View) : ViewHolder(view) {
        val binding:ItemReadQuranBinding by viewBinding()
        fun bindView(quran: Quran, totalAyah: Int, context:Context) {
            setTextViewValues(quran, totalAyah, context)
            setViewClickListener()
        }

        private fun setTextViewValues(quran: Quran, totalAyah: Int, context: Context) {
            binding.headerSurahName.isVisible = quran.ayahNumber == 1
            binding.txtSurahNameEn.text = quran.surahNameEn
            binding.txtDescendPlace.text = quran.turunSurah
            binding.txtAyah.text = applyTajweed(quran, context)
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

        private fun reverseAyahNumber(quran: Quran): String {
            val digit = mutableListOf<Char>()
            quran.ayahText!!.forEach {
                if (it.isDigit()) {
                    digit.add(it)
                }
            }
            val ayahNumberArabic = digit.joinToString("")
            val textWithoutNumber = quran.ayahText.replace(ayahNumberArabic, "")
            return textWithoutNumber + digit.reversed().joinToString("")
        }

        private fun applyTajweed(quran: Quran, context: Context) : Spannable {
            val ayahText = reverseAyahNumber(quran)
            return TajweedHelper.getTajweed(context, ayahText)
        }
    }
}