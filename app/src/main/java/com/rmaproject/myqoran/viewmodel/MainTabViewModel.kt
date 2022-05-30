package com.rmaproject.myqoran.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainTabViewModel : ViewModel() {
    private val tabPosition = MutableLiveData<Int>()
    private val totalAyah = MutableLiveData<Int>()

    fun setTabPosition(value:Int) {
        tabPosition.value = value
    }

    fun getTabPosition() : Int {
        return tabPosition.value?: 0
    }

    fun setTotalAyah(value:Int) {
        totalAyah.value = value
    }

    fun getTotalAyah() : Int {
        return totalAyah.value?: 0
    }
}