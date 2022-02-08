package com.yenaly.weatherchan.ui.place

import androidx.lifecycle.ViewModel

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

}