package com.yenaly.weatherchan.logic.network

import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.CurrentIpResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 通过Retrofit2获取IP对应地区。
 */

interface CurrentIpService {

    @GET("v3/ip?key=${WeatherChanApplication.KEY}")
    fun getCurrentIP(): Call<CurrentIpResponse>

}