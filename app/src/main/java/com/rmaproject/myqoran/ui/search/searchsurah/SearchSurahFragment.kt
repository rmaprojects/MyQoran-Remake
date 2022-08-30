package com.rmaproject.myqoran.ui.search.searchsurah

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentSearchSurahBinding
import com.rmaproject.myqoran.ui.read.ReadFragment
import com.rmaproject.myqoran.ui.search.searchsurah.adapter.SearchSurahAdapter
import com.rmaproject.myqoran.viewmodel.ValuesViewModel

class SearchSurahFragment : Fragment(R.layout.fragment_search_surah) {

    private val binding:FragmentSearchSurahBinding by viewBinding()
    private val valuesViewModel by activityViewModels<ValuesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)
        showOrHideKeyboard(true)

        binding.run {
            txtInputQuerySearchSurah.editText?.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = txtInputQuerySearchSurah.editText?.text
                    setSearchAdapter(query.toString())
                }
                true
            }
            appbarToolbar.setNavigationOnClickListener {
                showOrHideKeyboard(false)
                findNavController().navigateUp()
            }
        }
    }

    private fun setSearchAdapter(query:String) {
        val db = QuranDatabase.getInstance(requireContext())
        val dao = db.quranDao()
        dao.searchSurah("%$query%").asLiveData().observe(viewLifecycleOwner) { listQuran ->
            val adapter = SearchSurahAdapter(listQuran)
            binding.run {
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
            adapter.clickSurahItemListener = { searchSurahResult ->
                val bundle = bundleOf(
                    ReadFragment.SURAH_NUMBER_KEY to searchSurahResult.surahNumber,
                    ReadFragment.TOTAL_INDEX to valuesViewModel.totalSurahInQoran,
                    ReadFragment.TAB_POSITION to ReadFragment.INDEX_BY_SURAH
                )
                findNavController().navigate(R.id.action_searchSurahFragment_to_nav_read_fragment, bundle)
            }
        }
    }

    private fun showOrHideKeyboard(show:Boolean) {
        //get IME
        val ime = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (show) {
            binding.txtInputQuerySearchSurah.requestFocus()
            ime.showSoftInput(binding.txtInputQuerySearchSurah, 0)
        } else {
            binding.txtInputQuerySearchSurah.clearFocus()
            ime.hideSoftInputFromWindow(binding.txtInputQuerySearchSurah.windowToken, 0)
        }
    }
}