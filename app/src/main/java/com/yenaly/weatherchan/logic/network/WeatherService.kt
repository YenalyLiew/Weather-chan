package com.yenaly.weatherchan.logic.network

import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.DailyWeatherResponse
import com.yenaly.weatherchan.logic.model.HourlyWeatherResponse
import com.yenaly.weatherchan.logic.model.RealtimeWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 通过Retrofit2获取实时天气和未来天气结果。
 */

interface WeatherService {

    @GET("v2.5/${WeatherChanApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String,
        @Query("unit") unit: String?
    ): Call<RealtimeWeatherResponse>

    @GET("v2.5/${WeatherChanApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String,
        @Query("unit") unit: String?
    ): Call<DailyWeatherResponse>

    @GET("v2.5/${WeatherChanApplication.TOKEN}/{lng},{lat}/hourly.json?hourlysteps=24")
    fun getHourlyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String,
        @Query("unit") unit: String?
    ): Call<HourlyWeatherResponse>
}