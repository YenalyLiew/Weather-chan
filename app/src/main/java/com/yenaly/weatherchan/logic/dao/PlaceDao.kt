package com.yenaly.weatherchan.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.PlaceResponse

object PlaceDao {

    fun savePlace(place: PlaceResponse.Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): PlaceResponse.Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, PlaceResponse.Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences(): SharedPreferences {
        return WeatherChanApplication.context.getSharedPreferences(
            "weather_chan",
            Context.MODE_PRIVATE
        )
    }
}