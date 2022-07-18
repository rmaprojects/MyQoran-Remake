package com.rmaproject.myqoran.api

import com.rmaproject.myqoran.api.model.jadwalsholat.JadwalSholatModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("day")
    suspend fun getJadwalSholat(
        @Query("latitude") latitude:String,
        @Query("longitude") longitude:String
    ): Response<JadwalSholatModel>

    companion object {
        private const val BASE_URL = "https://prayer-times-xi.vercel.app/api/prayer/"

        fun createApi() : ApiInterface {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}