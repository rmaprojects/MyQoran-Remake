package com.rmaproject.myqoran.ui.search.searchayah

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
import com.rmaproject.myqoran.databinding.FragmentSearchAyahBinding
import com.rmaproject.myqoran.ui.search.searchayah.adapter.SearchAyahAdapter
import com.rmaproject.myqoran.ui.settings.preferences.SettingsPreferences

class SearchAyahFragment : Fragment(R.layout.fragment_search_ayah) {

    private val binding:FragmentSearchAyahBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)

        with(binding) {
            txtInputQuerySearchAyah.editText?.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = txtInputQuerySearchAyah.editText?.text.toString()
                    setSearchAdapter(query)
                }
                true
            }
            appbarToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setSearchAdapter(query: String) {
        val db = QuranDatabase.getInstance(requireContext())
        val dao = db.quranDao()
        if (SettingsPreferences.languagePreference == 0) {
            dao.searchEntireQuranId("%$query%").asLiveData().observe(viewLifecycleOwner) { listQuran ->
                binding.recylerView.adapter = SearchAyahAdapter(listQuran)
                binding.recylerView.layoutManager = LinearLayoutManager(requireContext())
            }
        } else {
            dao.searchEntireQuranEn("%$query%").asLiveData().observe(viewLifecycleOwner) { listQuran ->
                binding.recylerView.adapter = SearchAyahAdapter(listQuran)
                binding.recylerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}