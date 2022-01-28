package com.yenaly.weatherchan.logic.network

import retrofit2.await

object WeatherChanNetwork {

    private val placeService = ServiceCreator.createInCaiYun(PlaceService::class.java)
    private val weatherService = ServiceCreator.createInCaiYun(WeatherService::class.java)
    private val currentIpService = ServiceCreator.createInGaoDe(CurrentIpService::class.java)

    suspend fun searchPlaces(query: String) =
        placeService.searchPlaces(query).await()

    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    suspend fun getCurrentIP() =
        currentIpService.getCurrentIP().await()

}