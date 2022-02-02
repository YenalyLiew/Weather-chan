package com.yenaly.weatherchan.logic.network

import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.CurrentIpWithPlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 通过Retrofit2获取IP对应地区。
 */

interface CurrentIpWithPlaceService {

    @GET("v5/ip?key=${WeatherChanApplication.KEY}&type=4")
    fun getCurrentIPWithPlace(
        @Query("ip") ip: String
    ): Call<CurrentIpWithPlaceResponse>

}