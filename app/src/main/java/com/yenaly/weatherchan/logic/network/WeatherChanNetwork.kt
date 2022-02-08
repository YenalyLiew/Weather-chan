package com.yenaly.weatherchan.logic.network

import retrofit2.await

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 构建本地数据库以及网络数据源访问入口，对请求进行封装。
 */

object WeatherChanNetwork {

    private val placeService =
        ServiceCreator.create<PlaceService>(ServiceCreator.BASE_URL_CAIYUN)
    private val weatherService =
        ServiceCreator.create<WeatherService>(ServiceCreator.BASE_URL_CAIYUN)

    suspend fun searchPlaces(query: String) =
        placeService.searchPlaces(query).await()

    suspend fun getRealtimeWeather(lng: String, lat: String, unit: String?) =
        weatherService.getRealtimeWeather(lng, lat, unit).await()

    suspend fun getDailyWeather(lng: String, lat: String, unit: String?) =
        weatherService.getDailyWeather(lng, lat, unit).await()

}