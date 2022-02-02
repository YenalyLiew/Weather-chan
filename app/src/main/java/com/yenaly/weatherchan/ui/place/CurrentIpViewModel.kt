package com.yenaly.weatherchan.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yenaly.weatherchan.logic.Repository
import com.yenaly.weatherchan.logic.model.IPWithCityAndProvince

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 获取当前城市的ViewModel。
 */

class CurrentIpViewModel : ViewModel() {

    var currentCity = ""
    var currentProvince = ""
    var currentLng = ""
    var currentLat = ""

    private val ipLiveData = MutableLiveData<IPWithCityAndProvince>()
    val currentIPLiveData = Transformations.switchMap(ipLiveData) {
        Repository.getCurrentIPWithPlace()
    }

    fun getCurrentIPWithPlace() {
        ipLiveData.value = ipLiveData.value
    }
}