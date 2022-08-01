package com.rmaproject.myqoran.ui.bookmark

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.BookmarkDatabase
import com.rmaproject.myqoran.database.dao.BookmarkDAO
import com.rmaproject.myqoran.database.model.Bookmark
import com.rmaproject.myqoran.databinding.FragmentBookmarkBinding
import com.rmaproject.myqoran.helper.SnackbarHelper
import com.rmaproject.myqoran.ui.bookmark.adapter.BookmarkRecylerViewAdapter
import com.rmaproject.myqoran.ui.bookmark.adapter.listener.SwipeToDeleteListener
import com.rmaproject.myqoran.ui.read.ReadFragment
import com.rmaproject.myqoran.viewmodel.ValuesViewModel
import kotlinx.coroutines.launch

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {

    private val binding: FragmentBookmarkBinding by viewBinding()
    private val valuesViewModel: ValuesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = BookmarkDatabase.getInstance(requireContext())
        val dao = db.bookmarkDao()

        dao.getBookmarks().asLiveData().observe(viewLifecycleOwner) { bookmarks ->
            val adapter = BookmarkRecylerViewAdapter(bookmarks)
            if (bookmarks.isEmpty()) {
                binding.txtEmptyBookmark.isVisible = true
                binding.recyclerViewBookmark.isVisible = false
            } else {
                binding.recyclerViewBookmark.isVisible = true
                binding.txtEmptyBookmark.isVisible = false
                setAdapter(adapter)
            }
            setupSwipeToDeleteCallback(dao, bookmarks)

            adapter.bookmarkClickListener = { bookmark ->
                goToReadPage(bookmark)
            }
        }
    }

    private fun goToReadPage(bookmark: Bookmark) {
        val bundle = bundleOf(
            ReadFragment.IS_FROM_BOOKMARK_KEY to true,
            ReadFragment.SURAH_NUMBER_KEY to bookmark.surahNumber,
            ReadFragment.TAB_POSITION to ReadFragment.INDEX_BY_SURAH,
            ReadFragment.BOOKMARK_AYAH_NUMBER_KEY to bookmark.ayatNumber,
            ReadFragment.TOTAL_INDEX to valuesViewModel.totalSurahInQoran
        )
        findNavController().navigate(R.id.action_bookmarkFragment_to_nav_read_fragment, bundle)
    }

    private fun setAdapter(adapter: BookmarkRecylerViewAdapter) {
        binding.recyclerViewBookmark.adapter = adapter
        binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupSwipeToDeleteCallback(dao: BookmarkDAO, bookmarks: List<Bookmark>) {
        val swipeToDeleteCallBack = object : SwipeToDeleteListener() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                lifecycleScope.launch {
                    dao.deleteBookmark(bookmarks[position])
                    binding.recyclerViewBookmark.adapter?.notifyItemRemoved(position)
                }
                SnackbarHelper.showSnackbarShort(
                    binding.root,
                    getString(R.string.txt_bookmark_removed),
                    "Ok"
                ) {}
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewBookmark)
    }
}