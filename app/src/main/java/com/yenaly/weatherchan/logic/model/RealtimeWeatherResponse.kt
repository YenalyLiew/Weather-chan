package com.yenaly.weatherchan.logic.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 彩云天气实时天气API的JSON格式。
 */

data class RealtimeWeatherResponse(
    val status: String,
    val result: Result
) {

    data class Result(val realtime: Realtime) {

        data class Realtime(
            val temperature: Float,
            val humidity: Float,
            val cloudrate: Float,
            val skycon: String,
            val visibility: Float,
            val wind: Wind,
            val pressure: Float,
            val precipitation: Precipitation,

            @SerializedName("apparent_temperature")
            val apparentTemperature: Float,

            @SerializedName("air_quality")
            val airQuality: AirQuality,

            @SerializedName("life_index")
            val lifeIndex: LifeIndex
        ) : Serializable {

            data class Wind(
                val speed: Float,
                val direction: Float
            ) : Serializable

            data class Precipitation(val local: Local) : Serializable {

                data class Local(val intensity: Double) : Serializable

            }

            data class AirQuality(
                val aqi: AQI,
                val description: Description
            ) : Serializable {

                data class AQI(val chn: Float) : Serializable

                data class Description(val chn: String) : Serializable

            }

            data class LifeIndex(
                val ultraviolet: UltraViolet,
                val comfort: Comfort
            ) : Serializable {

                data class UltraViolet(val desc: String) : Serializable

                data class Comfort(val desc: String) : Serializable

            }
        }
    }
}