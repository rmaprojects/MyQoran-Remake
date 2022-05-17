package com.rmaproject.myqoran.ui.home.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rmaproject.myqoran.ui.home.index.IndexByJuzFragment
import com.rmaproject.myqoran.ui.home.index.IndexByPageFragment
import com.rmaproject.myqoran.ui.home.index.IndexBySurahFragment

class ViewPagerAdapter(context: FragmentActivity) :  FragmentStateAdapter(context){
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                IndexBySurahFragment()
            }
            1 -> {
                IndexByJuzFragment()
            }
            2 -> {
                IndexByPageFragment()
            }
            else -> throw Exception("Position not found")
        }
    }
}