package com.rmaproject.myqoran.ui.read.adapter.recyclerview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import com.l4digital.fastscroll.FastScroller
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.Bookmark
import com.rmaproject.myqoran.database.model.Quran
import com.rmaproject.myqoran.databinding.ItemReadQuranBinding
import com.rmaproject.myqoran.helper.SnackbarHelper
import com.rmaproject.myqoran.helper.TajweedHelper
import com.rmaproject.myqoran.ui.read.adapter.recyclerview.RecyclerViewReadQuranAdapter.RecyclerViewReadQuranAdapterViewHolder
import com.rmaproject.myqoran.ui.settings.preferences.RecentReadPreferences
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences
import com.skydoves.powermenu.MenuAnimation.SHOWUP_TOP_RIGHT
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class RecyclerViewReadQuranAdapter(
    private val listQuran: List<Quran>,
    private val listTotalAyah: List<Int>,
    private val indexType: Int,
) : Adapter<RecyclerViewReadQuranAdapterViewHolder>(), FastScroller.SectionIndexer {

    var footNoteonClickListener: ((String) -> Unit)? = null
    var addBookmarkClickListener: ((Bookmark) -> Unit)? = null
    var playAllAyahOnClickListener: ((List<Quran>, Int) -> Unit)? = null
    var playAyahOnClickListener: ((List<Quran>, Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewReadQuranAdapterViewHolder {
        return RecyclerViewReadQuranAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_read_quran, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerViewReadQuranAdapterViewHolder, position: Int) {
        val quran = listQuran[position]
        val totalAyah = listTotalAyah[quran.surahNumber!! - 1]
        val context = holder.itemView.context
        captureLastReadQuran(quran, position)
        holder.bindView(
            quran,
            totalAyah,
            context,
            indexType,
            addBookmarkClickListener,
            playAllAyahOnClickListener,
            playAyahOnClickListener,
            listQuran,
            position
        )
        holder.footNoteTracker(holder.itemView, quran, footNoteonClickListener, SettingsPreferences)
    }

    private fun captureLastReadQuran(quran: Quran, quranIndex: Int) {
        RecentReadPreferences.apply {
            lastReadSurah = "${quran.surahNameEn}"
            lastReadAyah = "${quran.ayahNumber}"
            lastReadSurahNumber = quran.surahNumber ?: 1
            lastReadJuzNumber = quran.juzNumber ?: 1
            lastReadPageNumber = quran.page ?: 1
            lastReadPosition = quranIndex
            position = indexType
        }
    }

    override fun getItemCount() = listQuran.size

    class RecyclerViewReadQuranAdapterViewHolder(val view: View) : ViewHolder(view) {
        val binding: ItemReadQuranBinding by viewBinding()
        fun bindView(
            quran: Quran,
            totalAyah: Int,
            context: Context,
            indexType: Int,
            addBookmarkClickListener: ((Bookmark) -> Unit)?,
            playAllAyahOnClickListener: ((List<Quran>, Int) -> Unit)?,
            playAyahOnClickListener: ((List<Quran>, Int) -> Unit)?,
            listQuran: List<Quran>,
            position: Int,
        ) {
            setTextViewValues(quran, totalAyah, context)
            setViewClickListener(
                context,
                quran,
                view,
                addBookmarkClickListener,
                playAyahOnClickListener,
                listQuran,
                position
            )
            applySettingsPreferences(
                listQuran,
                context,
                indexType,
                position,
                playAllAyahOnClickListener
            )
        }

        private fun applySettingsPreferences(
            listQuran: List<Quran>,
            context: Context,
            indexType: Int,
            position: Int,
            playAllAyahOnClickListener: ((List<Quran>, Int) -> Unit)?
        ) {
            val preferences = SettingsPreferences
            val quran = listQuran[position]
            binding.run {
                txtTranslate.isVisible = !preferences.isOnFocusRead
                txtAyah.text = when (preferences.showTajweed) {
                    false -> applyTajweed(quran, context)
                    true -> reverseAyahNumber(quran)
                }
                btnPlayAllAyah.isVisible = indexType == 0
                btnPlayAllAyah.setOnClickListener {
                    playAllAyahOnClickListener?.invoke(listQuran, position)
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

        private fun applyTajweed(quran: Quran, context: Context): Spannable {
            val ayahText = reverseAyahNumber(quran)
            return TajweedHelper.getTajweed(context, ayahText)
        }

        fun footNoteTracker(
            view: View,
            quran: Quran,
            footNoteonClickListener: ((String) -> Unit)?,
            preferences: SettingsPreferences
        ) {
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
            while (matcher.find()) {
                val clickableOpen = object : ClickableSpan() {
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = colorPrimary
                        ds.isUnderlineText = true
                    }

                    override fun onClick(p0: View) {
                        footNoteonClickListener?.invoke(
                            when (preferences.languagePreference) {
                                0 -> quran.footnotes_id ?: ""
                                1 -> quran.footnotes_en ?: ""
                                else -> {
                                    "Error"
                                }
                            }
                        )
                    }
                }
                spannable.setSpan(
                    clickableOpen,
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                spannable.setSpan(
                    RelativeSizeSpan(0.8F),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                spannable.setSpan(
                    SuperscriptSpan(),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
            binding.txtTranslate.movementMethod = LinkMovementMethod.getInstance()
            binding.txtTranslate.setText(spannable, TextView.BufferType.SPANNABLE)
        }

        private fun setTextViewValues(quran: Quran, totalAyah: Int, context: Context) {
            binding.apply {
                headerSurahName.isVisible = quran.ayahNumber == 1
                txtSurahNameEn.text = quran.surahNameEn
                txtDescendPlace.text = quran.turunSurah
                txtSurahNameAr.text = quran.surahNameAr
                txtTotalAyah.text = "$totalAyah ${context.getString(R.string.txt_ayah)}"
                txtTranslate.text = quran.translation_id
            }
        }

        private fun setViewClickListener(
            context: Context,
            quran: Quran,
            view: View,
            addBookmarkClickListener: ((Bookmark) -> Unit)?,
            playAyahOnClickListener: ((List<Quran>, Int) -> Unit)?,
            listQuran: List<Quran>,
            position: Int
        ) {
            binding.run {
                btnPlayAllAyah.setOnClickListener {
                    SnackbarHelper.showSnackbarShort(
                        binding.root,
                        context.getString(R.string.txt_play_all_ayah),
                        "Ok"
                    ) {}
                }
                txtAyah.setOnLongClickListener {
                    PowerMenu.Builder(context).apply {
                        setAnimation(SHOWUP_TOP_RIGHT)
                        setMenuRadius(32F)
                        setAutoDismiss(true)
                        setMenuColor(MaterialColors.getColor(view, android.R.attr.colorBackground))
                        setTextColor(MaterialColors.getColor(view, android.R.attr.colorPrimary))
                        setIconColor(MaterialColors.getColor(view, android.R.attr.colorPrimary))
                        setTextSize(18)
                        setWidth(650)
                        addItemList(getPowerItemList(context))
                        setOnMenuItemClickListener { menuPosition, _ ->
                            when (menuPosition) {
                                PLAY_AYAH -> playAyah(playAyahOnClickListener, listQuran, position)
                                COPY_AYAH -> copyAyah(context, quran)
                                SHARE_AYAH -> shareAyah(context, quran)
                                BOOKMARK_AYAH -> insertBookmark(
                                    quran,
                                    position,
                                    addBookmarkClickListener
                                )
                            }
                        }
                    }.build().showAsDropDown(txtAyah)
                    true
                }
            }
        }

        private fun getPowerItemList(context:Context): List<PowerMenuItem> {
            return listOf(
                PowerMenuItem(context.getString(R.string.txt_play_ayah), R.drawable.ic_baseline_play_circle_filled_24, false),
                PowerMenuItem(context.getString(R.string.txt_copy_ayah), R.drawable.ic_baseline_content_copy_24, false),
                PowerMenuItem(context.getString(R.string.txt_share_ayah), R.drawable.ic_baseline_share_24, false),
                PowerMenuItem(context.getString(R.string.txt_bookmark_ayah), R.drawable.ic_baseline_bookmark_24, false),
            )
        }

        private fun shareAyah(context: Context, quran: Quran) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, quran.surahNameEn)
            shareIntent.putExtra(Intent.EXTRA_TEXT, quran.ayahText)
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        private fun copyAyah(context: Context, quran: Quran) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData
            when (SettingsPreferences.languagePreference) {
                0 -> {
                    clip = ClipData.newPlainText(
                        quran.surahNameEn,
                        "${quran.ayahText}\n\n${quran.translation_id}"
                    )
                    clipboard.setPrimaryClip(clip)
                }
                1 -> {
                    clip = ClipData.newPlainText(
                        quran.surahNameEn,
                        "${quran.ayahText}\n\n${quran.translation_en}"
                    )
                    clipboard.setPrimaryClip(clip)
                }
            }
            SnackbarHelper.showSnackbarShort(
                binding.root,
                context.getString(R.string.txt_succeed_copy_ayah),
                "Ok"
            ) {}
        }

        private fun insertBookmark(
            quran: Quran,
            position: Int,
            addBookmarkClickListener: ((Bookmark) -> Unit)?
        ) {
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val bookmark = Bookmark(
                null,
                quran.surahNameEn,
                quran.ayahNumber,
                quran.surahNumber,
                position,
                quran.ayahText,
                currentDate,
                Date().time
            )
            addBookmarkClickListener?.invoke(bookmark)
            SnackbarHelper.showSnackbarShort(
                binding.root,
                "Surat ${quran.surahNameEn}: ${quran.ayahNumber} berhasil ditambahkan ke bookmark",
                "Ok"
            ) {}
        }

        private fun playAyah(
            playAyahOnClickListener: ((List<Quran>, Int) -> Unit)?,
            listQuran: List<Quran>,
            position:Int
        ) {
            playAyahOnClickListener?.invoke(listQuran, position)
        }

        companion object {
            private const val PLAY_AYAH = 0
            private const val COPY_AYAH = 1
            private const val SHARE_AYAH = 2
            private const val BOOKMARK_AYAH = 3
        }

    }

    override fun getSectionText(position: Int): String {
        val quran = listQuran[position]
        return "${quran.surahNameEn}:${quran.ayahNumber}"
    }
}