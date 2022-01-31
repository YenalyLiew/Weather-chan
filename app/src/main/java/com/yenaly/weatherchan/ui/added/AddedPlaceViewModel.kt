package com.yenaly.weatherchan.ui.added

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yenaly.weatherchan.logic.Repository
import com.yenaly.weatherchan.logic.model.PlaceResponse

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : [AddedPlaceFragment]的[ViewModel]，数据能暂时储存。
 */

class AddedPlaceViewModel : ViewModel() {
    private val addedPlaces = ArrayList<PlaceResponse.Place>()
    val modifyLiveData = MutableLiveData<List<PlaceResponse.Place>>()

    /**
     * 将用[addPlace]后刚添加的地区与[addPlacesInitialize]后的地区一并加入SharedPreferences。
     */
    private fun addPlaces() = Repository.addPlaces(addedPlaces)

    fun refreshPlaces() {
        modifyLiveData.value = addedPlaces
    }

    /**
     * List的add()方法和[addPlaces]方法合并，实现SharedPreferences该列数据实时更新。
     */
    fun addPlace(place: PlaceResponse.Place) {
        addedPlaces.add(place)
        addPlaces()
    }


    /**
     * List的remove()方法和[addPlaces]方法合并，实现SharedPreferences该列数据实时更新。
     */
    fun deletePlace(place: PlaceResponse.Place) {
        addedPlaces.remove(place)
        addPlaces()
    }

    /**
     * 给[addedPlaces]进行初始化，一般位于程序开始时调用。
     *
     * 调用该方法后，所有之前保存在SharedPreferences的该列[List]数据会
     * addAll 进入[addedPlaces]。若进行[addPlace]或[deletePlace]，
     * 不会造成SharedPreferences中该列[List]仅被新数据覆盖。
     */
    fun addPlacesInitialize() = addedPlaces.addAll(getAddedPlaces())

    /**
     * 与[addPlacesInitialize]方法配合使用。
     *
     * 从SharedPreferences中该列获取[List]信息。
     */
    private fun getAddedPlaces() = Repository.getAddedPlaces()

    fun isListNotEmpty() = addedPlaces.isNotEmpty()

    /**
     * 判断当前地区是否在[addedPlaces]列表里。
     */
    fun isPlaceAdded(place: PlaceResponse.Place, places: List<PlaceResponse.Place> = addedPlaces) =
        Repository.isPlaceAdded(place, places)

    /**
     * 判断SharedPreferences里是否含有该列。
     */
    fun isPlacesAdded() = Repository.isPlacesAdded()
}