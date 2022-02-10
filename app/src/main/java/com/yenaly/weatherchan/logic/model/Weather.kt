package com.yenaly.weatherchan.logic.model

import java.io.Serializable

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 将实时天气和未来天气串联起来的Weather类。
 */

class Weather(
    val hourly: HourlyWeatherResponse.Result.Hourly,
    val realtime: RealtimeWeatherResponse.Result.Realtime,
    val daily: DailyWeatherResponse.Result.Daily
) : Serializable