package com.yenaly.weatherchan.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : Retrofit构建器。
 */

object ServiceCreator {

    const val BASE_URL_CAIYUN = "https://api.caiyunapp.com/"
    const val BASE_URL_GAODE = "https://restapi.amap.com/"

    fun <T> create(url: String, serviceClass: Class<T>): T =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(serviceClass)

    inline fun <reified T> create(url: String): T = create(url, T::class.java)
}