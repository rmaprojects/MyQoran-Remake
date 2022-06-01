package com.rmaproject.myqoran.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmaproject.myqoran.database.model.Surah

class MainTabViewModel : ViewModel() {
    private val tabPosition = MutableLiveData<Int>()

    fun setTabPosition(value:Int) {
        tabPosition.value = value
    }

    fun getTabPosition() : Int {
        return tabPosition.value?: 0
    }

    private val totalAyahs: MutableLiveData<List<Int>> = MutableLiveData()

    fun getTotalAyahList(): MutableLiveData<List<Int>> = totalAyahs.value?.let { totalAyahs } ?: MutableLiveData()


    fun setTotalAyahList(surahList:List<Surah>) {
        val listTotalAyahs = mutableListOf<Int>()
        surahList.forEach {
            val numberOfAyah = it.numberOfAyah?:1
            listTotalAyahs.add(numberOfAyah)
        }
        totalAyahs.value = listTotalAyahs
    }
}