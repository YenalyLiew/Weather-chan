package com.yenaly.weatherchan.logic.model

data class Weather(
    val realtime: RealtimeWeatherResponse.Result.Realtime,
    val daily: DailyWeatherResponse.Result.Daily
)