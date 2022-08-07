package com.rmaproject.myqoran.ui.search.searchsurah

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.database.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentSearchSurahBinding
import com.rmaproject.myqoran.ui.search.searchsurah.adapter.SearchSurahAdapter

class SearchSurahFragment : Fragment(R.layout.fragment_search_surah) {

    private val binding:FragmentSearchSurahBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)

        binding.run {
            txtInputQuerySearchSurah.editText?.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val query = txtInputQuerySearchSurah.editText?.text.toString()
                    setSearchAdapter(query)
                }
                false
            }
        }
    }

    private fun setSearchAdapter(query:String) {
        val db = QuranDatabase.getInstance(requireContext())
        val dao = db.quranDao()
        dao.searchSurah(query).asLiveData().observe(viewLifecycleOwner) { listQuran ->
            val adapter = SearchSurahAdapter(listQuran)
            binding.run {
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }

            adapter.clickSurahItemListener = { quran ->
                findNavController().navigate(R.id.action_searchSurahFragment_to_nav_read_fragment)
            }
        }
    }
}