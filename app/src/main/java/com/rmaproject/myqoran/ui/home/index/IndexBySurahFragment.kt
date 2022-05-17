package com.rmaproject.myqoran.ui.home.index

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentIndexSurahBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexBySurahAdapter
import kotlinx.coroutines.launch

class IndexBySurahFragment : Fragment(R.layout.fragment_index_surah) {

    private val binding: FragmentIndexSurahBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = QuranDatabase.getInstance(requireContext())
        val quranDao = database.quranDao()
        quranDao.showQuranIndexBySurah().asLiveData().observe(viewLifecycleOwner) { surah ->
            val adapter = IndexBySurahAdapter(surah)
            setAdapter(adapter)
        }
    }

    private fun setAdapter(adapter: IndexBySurahAdapter) {
        binding.recyclerView.setAdapter(adapter)
    }
}