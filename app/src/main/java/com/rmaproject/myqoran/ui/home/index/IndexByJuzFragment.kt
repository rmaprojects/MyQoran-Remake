package com.rmaproject.myqoran.ui.home.index

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentIndexJuzBinding
import com.rmaproject.myqoran.ui.home.HomeViewModel
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexByJuzAdapter
import kotlinx.coroutines.launch

class IndexByJuzFragment : Fragment(R.layout.fragment_index_juz) {

    private val binding :FragmentIndexJuzBinding by viewBinding()
    private val homeViewModel:HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = QuranDatabase.getInstance(requireContext())
        val quranDao = database.quranDao()

        quranDao.showQuranIndexByJuz().asLiveData().observe(viewLifecycleOwner) { juz ->
            quranDao.getLastSurahOfJuz().asLiveData().observe(viewLifecycleOwner) { lastSurahOfJuzList ->
                val adapter = IndexByJuzAdapter(juz, lastSurahOfJuzList)
                setAdapter(adapter)
            }
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

    private fun setAdapter(adapter: IndexByJuzAdapter) {
        binding.recyclerView.adapter = adapter
    }
}