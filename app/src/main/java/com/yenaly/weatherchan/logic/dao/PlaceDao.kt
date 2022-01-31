package com.yenaly.weatherchan.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.PlaceResponse

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 使用SharedPreferences将点击后的城市记录储存，
 *                应用重启后会直接进入之前点击后的城市。
 */

object PlaceDao {

    /**
     * 将点击后的城市以JSON形式保存。
     */
    fun savePlace(place: PlaceResponse.Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    /**
     * 将以JSON形式储存的城市转化为[PlaceResponse.Place]格式。
     */
    fun getSavedPlace(): PlaceResponse.Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, PlaceResponse.Place::class.java)
    }

    /**
     * 判断当前地区是否被添加。
     */
    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences(): SharedPreferences {
        return WeatherChanApplication.context.getSharedPreferences(
            "weather_chan",
            Context.MODE_PRIVATE
        )
    }
}