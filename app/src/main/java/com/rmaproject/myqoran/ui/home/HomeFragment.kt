package com.rmaproject.myqoran.ui.home

import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentHomeBinding
import com.rmaproject.myqoran.ui.home.adapter.viewpager.ViewPagerAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        setAdapter(viewPagerAdapter)
        binding.openDrawerBtn.setOnClickListener {
            requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout).open()
        }
    }

    private fun setAdapter(adapter: ViewPagerAdapter) {
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Surah"
                1 -> "Juz"
                2 -> "Page"
                else -> "Unknown"
            }
        }.attach()
    }
}