package com.yenaly.weatherchan.logic.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/02/09 009 20:04
 * @Description : 彩云天气的每小时天气API的JSON格式。
 */

data class HourlyWeatherResponse(
    val status: String,
    val result: Result
) {
    data class Result(
        val hourly: Hourly
    ) {
        data class Hourly(
            val temperature: List<Temperature>,
            val precipitation: List<Precipitation>,
            val skycon: List<Skycon>,

            @SerializedName("air_quality")
            val airQuality: AirQuality
        ) : Serializable {

            data class Temperature(
                val value: Float
            ) : Serializable

            data class Precipitation(
                val value: String
            ) : Serializable

            data class Skycon(
                val value: String
            ) : Serializable

            data class AirQuality(
                val aqi: List<AQI>
            ) : Serializable {

                data class AQI(
                    val value: Value
                ) : Serializable {

                    data class Value(
                        val chn: String
                    ) : Serializable
                }
            }
        }
    }
}
