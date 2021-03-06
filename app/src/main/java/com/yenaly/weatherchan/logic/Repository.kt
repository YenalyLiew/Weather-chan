package com.yenaly.weatherchan.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.amap.api.location.AMapLocationListener
import com.yenaly.weatherchan.logic.dao.AddedPlaceDao
import com.yenaly.weatherchan.logic.dao.PlaceDao
import com.yenaly.weatherchan.logic.dao.SettingsDao
import com.yenaly.weatherchan.logic.model.PlaceResponse
import com.yenaly.weatherchan.logic.model.Weather
import com.yenaly.weatherchan.logic.network.WeatherChanNetwork
import com.yenaly.weatherchan.logic.utility.CurrentLocation
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

    fun refreshWeather(lng: String, lat: String, unit: String?): LiveData<Result<Weather>> {
        return fire {
            coroutineScope {
                val deferredRealtime =
                    async { WeatherChanNetwork.getRealtimeWeather(lng, lat, unit) }
                val deferredDaily = async { WeatherChanNetwork.getDailyWeather(lng, lat, unit) }
                val deferredHourly = async { WeatherChanNetwork.getHourlyWeather(lng, lat, unit) }
                val realtimeWeatherResponse = deferredRealtime.await()
                val dailyWeatherResponse = deferredDaily.await()
                val hourlyWeatherResponse = deferredHourly.await()
                if (realtimeWeatherResponse.status == "ok" &&
                    dailyWeatherResponse.status == "ok" &&
                    hourlyWeatherResponse.status == "ok"
                ) {
                    val weather = Weather(
                        hourlyWeatherResponse.result.hourly,
                        realtimeWeatherResponse.result.realtime,
                        dailyWeatherResponse.result.daily
                    )
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "Realtime Response is ${realtimeWeatherResponse.status}" +
                                    ", Daily Response is ${dailyWeatherResponse.status}" +
                                    ", Hourly Response is ${hourlyWeatherResponse.status}"
                        )
                    )
                }
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

    fun getSettingsString(string: String, defValue: String) =
        SettingsDao.getSettingsString(string, defValue)

    fun getOnceLocation(aMapLocationListener: AMapLocationListener) =
        CurrentLocation.getOnceLocation(aMapLocationListener)

    fun startLocation() = CurrentLocation.startLocation()

    fun destroyLocation() = CurrentLocation.destroyLocation()

    /**
     * [fire]函数先调用[liveData]函数，再在[liveData]代码块中统一进行`try` `catch`处理，
     * 在`try`语句调用传入的Lambda表达式中代码，最后获取Lambda表达式的执行结果，
     * 并调用`emit()`方法发射出去。
     *
     * @param context [CoroutineContext]默认值为[Dispatchers.IO]（子线程）。
     * @param block 要执行的代码块。
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
