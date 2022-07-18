package com.rmaproject.myqoran.api.model.jadwalsholat


import com.google.gson.annotations.SerializedName

data class JadwalSholatModel(
    @SerializedName("city")
    val city: String?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("lng")
    val lng: Double?,
    @SerializedName("longitude")
    val longitude: String?,
    @SerializedName("province")
    val province: String?,
    @SerializedName("times")
    val times: List<Time?>?
)