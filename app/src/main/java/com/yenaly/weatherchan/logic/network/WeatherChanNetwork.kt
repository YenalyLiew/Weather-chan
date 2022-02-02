package com.yenaly.weatherchan.logic.network

import retrofit2.await

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 构建本地数据库以及网络数据源访问入口，对请求进行封装。
 */

object WeatherChanNetwork {

    private val placeService = ServiceCreator.createInCaiYun(PlaceService::class.java)
    private val weatherService = ServiceCreator.createInCaiYun(WeatherService::class.java)
    private val currentIpService = ServiceCreator.createIpApi(CurrentIpService::class.java)
    private val currentIpWithPlaceService =
        ServiceCreator.createInGaoDe(CurrentIpWithPlaceService::class.java)

    suspend fun searchPlaces(query: String) =
        placeService.searchPlaces(query).await()

    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    suspend fun getCurrentIP() = currentIpService.getCurrentIP().await()

    suspend fun getCurrentIPWithPlace(ip: String) =
        currentIpWithPlaceService.getCurrentIPWithPlace(ip).await()

}