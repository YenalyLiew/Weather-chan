package com.yenaly.weatherchan.logic.network

import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 通过Retrofit2获取搜索城市结果。
 */

interface PlaceService {

    @GET("v2/place?token=${WeatherChanApplication.TOKEN}")
    fun searchPlaces(
        @Query("query") query: String
    ): Call<PlaceResponse>

}