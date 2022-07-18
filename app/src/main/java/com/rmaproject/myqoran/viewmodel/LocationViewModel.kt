package com.rmaproject.myqoran.viewmodel

import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    private var latitude:Double = -6.1752425
    private var longitude:Double = 106.8376195

    fun setLatitude(latitude:Double) {
        this.latitude = latitude
    }

    fun getLatitude() : Double {
        return this.latitude
    }

    fun setLongitude(longitude:Double) {
        this.longitude = longitude
    }

    fun getLongitude() : Double {
        return this.longitude
    }
}