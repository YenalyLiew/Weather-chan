package com.yenaly.weatherchan.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.yenaly.weatherchan.logic.dao.PlaceDao
import com.yenaly.weatherchan.logic.model.IPWithCityAndProvince
import com.yenaly.weatherchan.logic.model.PlaceResponse
import com.yenaly.weatherchan.logic.model.Weather
import com.yenaly.weatherchan.logic.network.WeatherChanNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchPlaces(query: String): LiveData<Result<List<PlaceResponse.Place>>> {
        return fire {
            val placeResponse = WeatherChanNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("Response Status is ${placeResponse.status}"))
            }
        }
    }

    fun refreshWeather(lng: String, lat: String): LiveData<Result<Weather>> {
        return fire {
            coroutineScope {
                val deferredRealtime = async { WeatherChanNetwork.getRealtimeWeather(lng, lat) }
                val deferredDaily = async { WeatherChanNetwork.getDailyWeather(lng, lat) }
                val realtimeWeatherResponse = deferredRealtime.await()
                val dailyWeatherResponse = deferredDaily.await()
                if (realtimeWeatherResponse.status == "ok" && dailyWeatherResponse.status == "ok") {
                    val weather = Weather(
                        realtimeWeatherResponse.result.realtime,
                        dailyWeatherResponse.result.daily
                    )
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "Realtime Response is ${realtimeWeatherResponse.status}" +
                                    ", Daily Response is ${dailyWeatherResponse.status}"
                        )
                    )
                }
            }
        }
    }

    fun getCurrentIP(): LiveData<Result<IPWithCityAndProvince>> {
        return fire {
            val currentIpResponse = WeatherChanNetwork.getCurrentIP()
            if (currentIpResponse.status == "1") {
                val currentIpRange = currentIpResponse.rectangle.split(";")
                val province = currentIpResponse.province
                val city = currentIpResponse.city
                Result.success(IPWithCityAndProvince(city, province, currentIpRange[0]))
            } else {
                Result.failure(RuntimeException("Response Status is ${currentIpResponse.status}"))
            }
        }
    }

    fun savePlace(place: PlaceResponse.Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    private fun <T> fire(
        context: CoroutineContext = Dispatchers.IO,
        block: suspend () -> Result<T>
    ): LiveData<Result<T>> {
        return liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    }
}