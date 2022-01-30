package com.yenaly.weatherchan.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.logic.model.PlaceResponse

object AddedPlaceDao {

    fun addPlaces(places: List<PlaceResponse.Place>) {
        sharedPreferences().edit {
            putString("added_places", Gson().toJson(places))
        }
    }

    fun getAddedPlaces(): List<PlaceResponse.Place> {
        val addedPlaceJson = sharedPreferences().getString("added_places", "")
        val turnsType = object : TypeToken<List<PlaceResponse.Place>>() {}.type
        return Gson().fromJson(addedPlaceJson, turnsType)
    }

    fun isPlaceAdded(place: PlaceResponse.Place, places: List<PlaceResponse.Place>): Boolean {
        return (place in places)
    }

    fun isPlacesAdded() = sharedPreferences().contains("added_places")

    private fun sharedPreferences(): SharedPreferences {
        return WeatherChanApplication.context.getSharedPreferences(
            "weather_chan",
            Context.MODE_PRIVATE
        )
    }
}