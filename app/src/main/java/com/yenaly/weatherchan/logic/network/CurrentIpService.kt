package com.yenaly.weatherchan.logic.network

import com.yenaly.weatherchan.logic.model.CurrentIpResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/01/31 031 21:43
 * @Description : 通过Retrofit2获取IP地址。
 */

interface CurrentIpService {

    @GET("json/")
    fun getCurrentIP(): Call<CurrentIpResponse>

}