package com.yenaly.weatherchan.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL_CAIYUN = "https://api.caiyunapp.com/"
    private const val BASE_URL_GAODE = "https://restapi.amap.com/"

    private val retrofitCaiyun =
        Retrofit.Builder()
            .baseUrl(BASE_URL_CAIYUN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val retrofitGaode =
        Retrofit.Builder()
            .baseUrl(BASE_URL_GAODE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun <T> createInCaiYun(serviceClass: Class<T>): T = retrofitCaiyun.create(serviceClass)
    fun <T> createInGaoDe(serviceClass: Class<T>): T = retrofitGaode.create(serviceClass)
}