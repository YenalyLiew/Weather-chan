package com.yenaly.weatherchan.logic.network

import com.google.gson.Gson
import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.*
import okhttp3.*
import java.lang.Exception

/**
 * @ProjectName : Weather-chan
 * @Author : 作者
 * @Time : 2022/02/03 003 22:14
 * @Description : 文件描述
 */
object WeatherNetwork {

    private const val BASE_URL_CAIYUN = "https://api.caiyunapp.com/"
    private const val BASE_URL_GAODE = "https://restapi.amap.com/"
    private const val BASE_URL_IP_API = "http://ip-api.com/"

    private val client = OkHttpClient()
    private val gson = Gson()

    fun searchPlaces(query: String): PlaceResponse? {
        var places: PlaceResponse? = null
        try {
            val request = Request.Builder()
                .url(BASE_URL_CAIYUN + "v2/place?token=${WeatherChanApplication.TOKEN}&query=$query")
                .get()
                .build()
            val response = client.newCall(request).execute()
            val responseData = response.body()?.string()
            if (responseData != null)
                places = gson.fromJson(responseData, PlaceResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return places
    }

    fun getRealtimeWeather(lng: String, lat: String): RealtimeWeatherResponse? {
        var realtimeWeather: RealtimeWeatherResponse? = null
        try {
            val request = Request.Builder()
                .url(BASE_URL_CAIYUN + "v2.5/${WeatherChanApplication.TOKEN}/$lng,$lat/realtime.json")
                .get()
                .build()
            val response = client.newCall(request).execute()
            val responseData = response.body()?.string()
            if (responseData != null)
                realtimeWeather = gson.fromJson(responseData, RealtimeWeatherResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return realtimeWeather
    }

    fun getDailyWeather(lng: String, lat: String): DailyWeatherResponse? {
        var dailyWeather: DailyWeatherResponse? = null
        try {
            val request = Request.Builder()
                .url(BASE_URL_CAIYUN + "v2.5/${WeatherChanApplication.TOKEN}/$lng,$lat/daily.json")
                .get()
                .build()
            val response = client.newCall(request).execute()
            val responseData = response.body()?.string()
            if (responseData != null)
                dailyWeather = gson.fromJson(responseData, DailyWeatherResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dailyWeather
    }

    fun getCurrentIP(): CurrentIpResponse? {
        var currentIP: CurrentIpResponse? = null
        try {
            val request = Request.Builder()
                .url(BASE_URL_IP_API + "json")
                .get()
                .build()
            val response = client.newCall(request).execute()
            val responseData = response.body()?.string()
            if (responseData != null)
                currentIP = gson.fromJson(responseData, CurrentIpResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currentIP
    }

    fun getCurrentIPWithPlace(ip: String): CurrentIpWithPlaceResponse? {
        var currentIPWithPlace: CurrentIpWithPlaceResponse? = null
        try {
            val request = Request.Builder()
                .url(BASE_URL_GAODE + "v5/ip?key=${WeatherChanApplication.KEY}&type=4&ip=$ip")
                .get()
                .build()
            val response = client.newCall(request).execute()
            val responseData = response.body()?.string()
            if (responseData != null)
                currentIPWithPlace =
                    gson.fromJson(responseData, CurrentIpWithPlaceResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currentIPWithPlace
    }
}