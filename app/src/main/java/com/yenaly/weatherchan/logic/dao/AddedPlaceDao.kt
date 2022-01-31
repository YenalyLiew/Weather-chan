package com.yenaly.weatherchan.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.PlaceResponse

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 使用SharedPreferences将添加的城市储存。
 */

object AddedPlaceDao {

    /**
     * 以Json方式存储List于[sharedPreferences]。
     */
    fun addPlaces(places: List<PlaceResponse.Place>) {
        sharedPreferences().edit {
            putString("added_places", Gson().toJson(places))
        }
    }

    /**
     * 获取List，用[TypeToken]将JSON转换回[List]。
     */
    fun getAddedPlaces(): List<PlaceResponse.Place> {
        val addedPlaceJson = sharedPreferences().getString("added_places", "")
        val turnsType = object : TypeToken<List<PlaceResponse.Place>>() {}.type
        return Gson().fromJson(addedPlaceJson, turnsType)
    }

    /**
     * 判断当前地区是否被添加。
     */
    fun isPlaceAdded(place: PlaceResponse.Place, places: List<PlaceResponse.Place>): Boolean {
        return (place in places)
    }

    /**
     * 判断[sharedPreferences]是否存在该列。
     */
    fun isPlacesAdded() = sharedPreferences().contains("added_places")

    private fun sharedPreferences(): SharedPreferences {
        return WeatherChanApplication.context.getSharedPreferences(
            "weather_chan",
            Context.MODE_PRIVATE
        )
    }
}