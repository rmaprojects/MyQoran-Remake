package com.rmaproject.myqoran.ui.home.index

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentIndexJuzBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexByJuzAdapter

class IndexByJuzFragment : Fragment(R.layout.fragment_index_juz) {

    private val binding :FragmentIndexJuzBinding by viewBinding()

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
    }
    private fun setAdapter(adapter: IndexByJuzAdapter) {
        binding.recyclerView.adapter = adapter
    }
}