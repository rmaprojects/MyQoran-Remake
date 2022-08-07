package com.rmaproject.myqoran.ui.search.searchayah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rmaproject.myqoran.R

class SearchAyahFragment : Fragment(R.layout.fragment_search_ayah) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)
    }
}