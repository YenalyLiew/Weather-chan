package com.yenaly.weatherchan.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yenaly.weatherchan.logic.Repository
import com.yenaly.weatherchan.logic.model.PlaceResponse

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : [PlaceSearchFragment]的[ViewModel]。前面是动态获取城市，
 *                实时更新城市列表的[LiveData]方法。后面是实现城市记录的方法。
 */

class PlaceViewModel : ViewModel() {

    val placeList = ArrayList<PlaceResponse.Place>()
    val modifyLiveData = MutableLiveData<List<PlaceResponse.Place>>()

    private val searchLiveData = MutableLiveData<String>()
    val placeLiveDataObserver = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    fun refreshSearch() {
        modifyLiveData.value = placeList
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    /**
     * 将地区点击记录信息保存于SharedPreferences的该列中。
     */
    fun savePlace(place: PlaceResponse.Place) = Repository.savePlace(place)

    /**
     * 从SharedPreferences获取地区点击记录信息。
     */
    fun getSavedPlace() = Repository.getSavedPlace()

    /**
     * 判断SharedPreferences里是否含有该列。
     */
    fun isPlaceSaved() = Repository.isPlaceSaved()

}