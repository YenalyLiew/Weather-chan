package com.yenaly.weatherchan.ui.place

import androidx.lifecycle.ViewModel
import com.amap.api.location.AMapLocationListener
import com.yenaly.weatherchan.logic.Repository

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 获取当前城市的ViewModel。
 */

class CurrentPlaceViewModel : ViewModel() {

    var currentCity = ""
    var currentProvince = ""
    var currentLng = ""
    var currentLat = ""

    /**
     * 获取当前位置信息，只获取一次。
     */
    fun getOnceLocation(aMapLocationListener: AMapLocationListener) =
        Repository.getOnceLocation(aMapLocationListener)

    fun startLocation() = Repository.startLocation()

    fun destroyLocation() = Repository.destroyLocation()
}