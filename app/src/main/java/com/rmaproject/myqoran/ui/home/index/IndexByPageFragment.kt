package com.rmaproject.myqoran.ui.home.index

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentIndexPageBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexByPageAdapter

class IndexByPageFragment : Fragment(R.layout.fragment_index_page) {

    private val binding:FragmentIndexPageBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = QuranDatabase.getInstance(requireContext())
        val quranDao = database.quranDao()

        quranDao.showQuranIndexByPage().asLiveData().observe(viewLifecycleOwner) { page ->
            quranDao.getLastAyahOfPage().asLiveData().observe(viewLifecycleOwner) { lastAyahList ->
                val adapter = IndexByPageAdapter(page, lastAyahList)
                setAdapter(adapter)
            }
        }
    }

    private fun setAdapter(adapter: IndexByPageAdapter) {
        binding.recyclerView.adapter = adapter
    }
}