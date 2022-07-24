package com.rmaproject.myqoran.ui.bookmark.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.l4digital.fastscroll.FastScroller
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.model.Bookmark
import com.rmaproject.myqoran.databinding.ItemBookmarkBinding
import com.rmaproject.myqoran.ui.bookmark.adapter.BookmarkRecylerViewAdapter.RecyclerViewAdapterViewHolder

class BookmarkRecylerViewAdapter(private val bookmarks:List<Bookmark>) : RecyclerView.Adapter<RecyclerViewAdapterViewHolder>(), FastScroller.SectionIndexer {

    var bookmarkClickListener:((Bookmark) -> Unit)?= null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapterViewHolder {
        return RecyclerViewAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapterViewHolder, position: Int) {
        val bookmark = bookmarks[position]
        holder.binding.clickableCardBookmark.setOnClickListener {
            bookmarkClickListener?.invoke(bookmark)
        }
        holder.bindView(bookmark, position)
    }

    override fun getItemCount() = bookmarks.size

    class RecyclerViewAdapterViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val binding : ItemBookmarkBinding by viewBinding()
        fun bindView(bookmark: Bookmark, position: Int) {
            binding.run {
                txtArabicText.text = bookmark.textQuran
                txtAyahPositionBookmark.text = "Ayat ${bookmark.ayatNumber}"
                txtDateAdded.text = bookmark.timeStamp
                txtSurahNameBookmark.text = bookmark.surahName
                txtIndexBookmark.text = (position + 1).toString()
            }
        }
    }

    override fun getSectionText(position: Int) = "Bookmark #$position"
}