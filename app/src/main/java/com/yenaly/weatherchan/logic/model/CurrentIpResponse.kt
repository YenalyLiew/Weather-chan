package com.yenaly.weatherchan.logic.model

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/01/31 031 21:09
 * @Description : IP-API的IP定位功能的JSON格式。
 */

data class CurrentIpResponse(
    val status: String,
    val query: String
)