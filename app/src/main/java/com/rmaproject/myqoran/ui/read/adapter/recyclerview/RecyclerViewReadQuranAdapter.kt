package com.rmaproject.myqoran.ui.read.adapter.recyclerview

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.color.MaterialColors
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.Quran
import com.rmaproject.myqoran.databinding.ItemReadQuranBinding
import com.rmaproject.myqoran.helper.SnackbarHelper
import com.rmaproject.myqoran.helper.TajweedHelper
import com.rmaproject.myqoran.ui.read.adapter.recyclerview.RecyclerViewReadQuranAdapter.RecyclerViewReadQuranAdapterViewHolder
import com.rmaproject.myqoran.ui.settings.preferences.RecentReadPreferences
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences
import java.util.regex.Pattern

class RecyclerViewReadQuranAdapter(
    private val listQuran: List<Quran>,
    private val listTotalAyah: List<Int>,
    private val indexType: Int,
) : Adapter<RecyclerViewReadQuranAdapterViewHolder>() {

    var footNoteonClickListener: ((String) -> Unit)? = null

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
        captureLastReadQuran(quran)
        holder.bindView(quran, totalAyah, context)
        holder.footNoteTracker(holder.itemView, quran, footNoteonClickListener, SettingsPreferences)
    }

    private fun captureLastReadQuran(quran:Quran) {
        RecentReadPreferences.apply {
            lastReadSurah = "${quran.surahNameEn}"
            lastReadAyah = "${quran.ayahNumber}"
            lastReadSurahNumber = quran.surahNumber?:1
            lastReadJuzNumber = quran.juzNumber?:1
            lastReadPageNumber = quran.page?:1
            lastReadPosition = (quran.surahNumber?:0) -1
            position = indexType
        }
    }

    override fun getItemCount() = listQuran.size

    class RecyclerViewReadQuranAdapterViewHolder(view:View) : ViewHolder(view) {
        val binding:ItemReadQuranBinding by viewBinding()
        fun bindView(quran: Quran, totalAyah: Int, context:Context) {
            setTextViewValues(quran, totalAyah)
            setViewClickListener(context)
            applySettingsPreferences(quran, context)
        }

        private fun applySettingsPreferences(quran: Quran, context: Context) {
            val preferences = SettingsPreferences

            binding.apply {
                txtTranslate.isVisible = !preferences.isOnFocusRead
                txtAyah.text = when (preferences.showTajweed) {
                    false -> applyTajweed(quran, context)
                    true -> reverseAyahNumber(quran)
                }
            }
        }

        private fun setTextViewValues(quran: Quran, totalAyah: Int) {
            binding.apply {
                headerSurahName.isVisible = quran.ayahNumber == 1
                txtSurahNameEn.text = quran.surahNameEn
                txtDescendPlace.text = quran.turunSurah
                txtSurahNameAr.text = quran.surahNameAr
                txtTotalAyah.text = "$totalAyah Ayah"
                txtTranslate.text = quran.translation_id
            }
        }

        private fun setViewClickListener(context: Context) {
            binding.apply {
                btnPlayAllAyah.setOnClickListener {
                    SnackbarHelper.showSnackbarShort(binding.root, context.getString(R.string.txt_play_all_ayah), "Ok") {}
                }
                txtAyah.setOnLongClickListener {
                    SnackbarHelper.showSnackbarShort(binding.root, "Long Click Ayah", "Ok") {}
                    true
                }
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

        fun footNoteTracker(view: View, quran:Quran, footNoteonClickListener: ((String) -> Unit)?, preferences:SettingsPreferences) {
            val colorPrimary = MaterialColors.getColor(view, android.R.attr.colorPrimary)
            val translate = when (preferences.languagePreference) {
                0 -> quran.translation_id
                1 -> {
                    val pattern = Pattern.compile("\\(([^)]+)\\)")
                    quran.translation_en
                    quran.translation_en?.let { pattern.matcher(it).replaceAll("") }
                }
                else -> Exception("Language not supported")
            }.toString()
            val spannable = SpannableStringBuilder(translate)
            val pattern = Pattern.compile("""[0-9]""", Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(translate)
            while (matcher.find()){
                val clickableOpen = object : ClickableSpan() {
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = colorPrimary
                        ds.isUnderlineText = true
                    }

                    override fun onClick(p0: View) {
                        footNoteonClickListener?.invoke(
                            when (preferences.languagePreference) {
                                0 -> quran.footnotes_id?:""
                                1 -> quran.footnotes_en?:""
                                else -> { "Error" }
                            }
                        )
                    }
                }
                spannable.setSpan(clickableOpen, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                spannable.setSpan(RelativeSizeSpan(0.8F), matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                spannable.setSpan(SuperscriptSpan(), matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
            binding.txtTranslate.movementMethod = LinkMovementMethod.getInstance()
            binding.txtTranslate.setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }
}