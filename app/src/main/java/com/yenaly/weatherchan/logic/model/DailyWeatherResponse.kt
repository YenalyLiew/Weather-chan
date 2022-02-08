package com.yenaly.weatherchan.logic.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 彩云天气的未来天气API的JSON格式。
 */

data class DailyWeatherResponse(
    val status: String,
    val result: Result
) {

    data class Result(val daily: Daily) {

        data class Daily(
            val temperature: List<Temperature>,
            val skycon: List<Skycon>,

            @SerializedName("life_index")
            val lifeIndex: LifeIndex
        ) : Serializable {

            data class Temperature(
                val max: Float,
                val min: Float
            ) : Serializable

            data class Skycon(
                val date: Date,
                val value: String
            ) : Serializable

            data class LifeIndex(
                val coldRisk: List<LifeDescription>,
                val carWashing: List<LifeDescription>,
                val ultraviolet: List<LifeDescription>,
                val dressing: List<LifeDescription>
            ) : Serializable {

                data class LifeDescription(val desc: String) : Serializable

            }
        }
    }
}
