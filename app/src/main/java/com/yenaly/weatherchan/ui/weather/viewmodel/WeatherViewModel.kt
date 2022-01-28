package com.yenaly.weatherchan.ui.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yenaly.weatherchan.logic.Repository
import com.yenaly.weatherchan.logic.model.PlaceResponse

class WeatherViewModel : ViewModel() {

    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    private val locationLiveData = MutableLiveData<PlaceResponse.Place.Location>()
    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = PlaceResponse.Place.Location(lng, lat)
    }
}