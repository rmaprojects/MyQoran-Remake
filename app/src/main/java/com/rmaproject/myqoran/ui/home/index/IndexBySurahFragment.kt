package com.rmaproject.myqoran.ui.home.index

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentIndexSurahBinding
import com.rmaproject.myqoran.ui.home.HomeViewModel
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexBySurahAdapter
import com.rmaproject.myqoran.ui.read.ReadFragment

class IndexBySurahFragment : Fragment(R.layout.fragment_index_surah) {

    private val binding: FragmentIndexSurahBinding by viewBinding()
    private val homeViewModel:HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = QuranDatabase.getInstance(requireContext())
        val quranDao = database.quranDao()
        quranDao.showQuranIndexBySurah().asLiveData().observe(viewLifecycleOwner) { surah ->
            val adapter = IndexBySurahAdapter(surah)
            setAdapter(adapter)
        }
        setFab()
    }

    private fun setFab() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    homeViewModel.hideFabListener?.invoke()
                } else {
                    homeViewModel.showFabListener?.invoke()
                }
            }
        })
        homeViewModel.goToTopListener = {
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }

    private fun setAdapter(adapter: IndexBySurahAdapter) {
        adapter.callBackToRead = { surah, surahTotal ->
            val bundle = bundleOf(
                ReadFragment.SURAH_NUMBER_KEY to surah.surahNumber,
                ReadFragment.TOTAL_INDEX to surahTotal
            )
            findNavController().navigate(R.id.action_nav_home_to_nav_read_fragment, bundle)
        }
        binding.recyclerView.adapter = adapter
    }
}