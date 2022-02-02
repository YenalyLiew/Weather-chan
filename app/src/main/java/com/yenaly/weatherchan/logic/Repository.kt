package com.yenaly.weatherchan.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.yenaly.weatherchan.logic.dao.AddedPlaceDao
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

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 应用的主仓库，进行UI层与Logic层之间的联系。
 */

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

    fun getCurrentIPWithPlace(): LiveData<Result<IPWithCityAndProvince>> {
        return fire {
            val currentIpResponse = WeatherChanNetwork.getCurrentIP()
            if (currentIpResponse.status == "success") {
                val ip = currentIpResponse.query
                val currentIpWithPlaceResponse = WeatherChanNetwork.getCurrentIPWithPlace(ip)
                if (currentIpWithPlaceResponse.status == "1") {
                    val country = currentIpWithPlaceResponse.country
                    val province = currentIpWithPlaceResponse.province
                    val city = currentIpWithPlaceResponse.city
                    val district = currentIpWithPlaceResponse.district
                    val location = currentIpWithPlaceResponse.location
                    Result.success(
                        IPWithCityAndProvince(
                            country,
                            province,
                            city,
                            district,
                            location
                        )
                    )
                } else {
                    Result.failure(RuntimeException("Place Response Status is ${currentIpWithPlaceResponse.status}"))
                }
            } else {
                Result.failure(RuntimeException("IP Response Status is ${currentIpResponse.status}"))
            }
        }
    }

    fun savePlace(place: PlaceResponse.Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    fun addPlaces(places: List<PlaceResponse.Place>) = AddedPlaceDao.addPlaces(places)

    fun getAddedPlaces() = AddedPlaceDao.getAddedPlaces()

    fun isPlaceAdded(place: PlaceResponse.Place, places: List<PlaceResponse.Place>) =
        AddedPlaceDao.isPlaceAdded(place, places)

    fun isPlacesAdded() = AddedPlaceDao.isPlacesAdded()

    /**
     * [fire]函数先调用[liveData]函数，再在[liveData]代码块中统一进行`try` `catch`处理，
     * 在`try`语句调用传入的Lambda表达式中代码，最后获取Lambda表达式的执行结果，
     * 并调用`emit()`方法发射出去。
     *
     * [CoroutineContext]默认值为[Dispatchers.IO]（子线程）。
     */
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