package com.yenaly.weatherchan.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 彩云天气搜索地区API的JSON格式。
 */

data class PlaceResponse(
    val status: String,
    val places: List<Place>
) {
    data class Place(
        val name: String,
        val location: Location,

        @SerializedName("formatted_address")
        val address: String
    ) {
        data class Location(
            val lng: String,
            val lat: String
        )
    }
}
