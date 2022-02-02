package com.yenaly.weatherchan.logic.model

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 高德API的IP定位功能的JSON格式。
 */

data class CurrentIpWithPlaceResponse(
    val status: String,
    val country: String,
    val province: String,
    val city: String,
    val district: String,
    val location: String
)
