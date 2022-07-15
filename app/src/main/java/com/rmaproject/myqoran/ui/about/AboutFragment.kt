package com.rmaproject.myqoran.ui.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentAboutBinding

class AboutFragment : Fragment(R.layout.fragment_about) {

    private val binding: FragmentAboutBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}