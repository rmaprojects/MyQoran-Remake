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
import com.rmaproject.myqoran.databinding.FragmentIndexJuzBinding
import com.rmaproject.myqoran.ui.home.adapter.recyclerview.IndexByJuzAdapter
import com.rmaproject.myqoran.ui.read.ReadFragment
import com.rmaproject.myqoran.viewmodel.MainTabViewModel

class IndexByJuzFragment : Fragment(R.layout.fragment_index_juz) {

    private val binding: FragmentIndexJuzBinding by viewBinding()
    private val viewModel: MainTabViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = QuranDatabase.getInstance(requireContext())
        val quranDao = database.quranDao()

        quranDao.showQuranIndexByJuz().asLiveData().observe(viewLifecycleOwner) { juz ->
            quranDao.getLastSurahOfJuz().asLiveData()
                .observe(viewLifecycleOwner) { lastSurahOfJuzList ->
                    val adapter = IndexByJuzAdapter(juz, lastSurahOfJuzList)
                    setAdapter(adapter)
                }
        }
    }

    private fun setAdapter(adapter: IndexByJuzAdapter) {
        binding.recyclerView.adapter = adapter
        adapter.listener = { juzList, totalAyah ->
            val bundle = bundleOf(
                ReadFragment.JUZ_NUMBER_KEY to juzList.juzNumber,
                ReadFragment.TOTAL_INDEX to totalAyah,
                ReadFragment.TAB_POSITION to viewModel.getTabPosition()
            )
            findNavController().navigate(R.id.action_nav_home_to_nav_read_fragment, bundle)
        }
    }
}