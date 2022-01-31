package com.yenaly.weatherchan.logic.model

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 将IP与城市和省份串联起来的IPWithCityAndProvince类。
 */

class IPWithCityAndProvince(
    val city: String,
    val province: String,
    val lngandlat: String
)