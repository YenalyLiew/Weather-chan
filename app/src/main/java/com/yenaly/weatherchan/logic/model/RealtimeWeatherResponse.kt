package com.yenaly.weatherchan.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeWeatherResponse(val status: String, val result: Result) {

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
        ) {

            data class Wind(
                val speed: Float,
                val direction: Float
            )

            data class Precipitation(val local: Local) {

                data class Local(val intensity: Double)

            }

            data class AirQuality(val aqi: AQI) {

                data class AQI(val chn: Float)

            }

            data class LifeIndex(
                val ultraviolet: UltraViolet,
                val comfort: Comfort
            ) {

                data class UltraViolet(val desc: String)

                data class Comfort(val desc: String)

            }
        }
    }
}