package com.rmaproject.myqoran.ui.home.index

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentIndexPageBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexByPageAdapter
import com.rmaproject.myqoran.ui.read.ReadFragment
import com.rmaproject.myqoran.viewmodel.MainTabViewModel

class IndexByPageFragment : Fragment(R.layout.fragment_index_page) {

    private val binding:FragmentIndexPageBinding by viewBinding()
    private val viewModel by activityViewModels<MainTabViewModel>()

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
        adapter.listener = { pageList, totalAyah ->
            val bundle = bundleOf(
                ReadFragment.PAGE_NUMBER_KEY to pageList.page,
                ReadFragment.TOTAL_INDEX to totalAyah,
                ReadFragment.TAB_POSITION to viewModel.getTabPosition()
            )
            findNavController().navigate(R.id.action_nav_home_to_nav_read_fragment, bundle)
        }
    }
}