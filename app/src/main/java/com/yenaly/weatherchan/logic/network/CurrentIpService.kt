package com.yenaly.weatherchan.logic.network

import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.CurrentIpResponse
import retrofit2.Call
import retrofit2.http.GET

interface CurrentIpService {

    @GET("v3/ip?key=${WeatherChanApplication.KEY}")
    fun getCurrentIP(): Call<CurrentIpResponse>

}