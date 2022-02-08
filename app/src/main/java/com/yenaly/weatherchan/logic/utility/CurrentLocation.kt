package com.yenaly.weatherchan.logic.utility

import android.annotation.SuppressLint
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.yenaly.weatherchan.WeatherChanApplication
import java.lang.Exception

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/02/08 008 10:47
 * @Description : 利用高德SDK获取当前地区信息。
 */

object CurrentLocation {

    //传入的是全局Context，所以内存不会泄露。
    @SuppressLint("StaticFieldLeak")
    private var locationClient: AMapLocationClient? =
        AMapLocationClient(WeatherChanApplication.context)

    private var locationClientOption: AMapLocationClientOption? = null

    /**
     * 获取当前位置信息，只获取一次。
     */
    fun getOnceLocation(aMapLocationListener: AMapLocationListener) {
        try {
            locationClientOption = AMapLocationClientOption()
            locationClientOption?.isOnceLocation = true
            locationClient?.setLocationOption(locationClientOption)
            locationClient?.setLocationListener(aMapLocationListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startLocation() {
        try {
            locationClient?.startLocation()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun destroyLocation() {
        try {
            locationClient?.onDestroy()
            locationClient = null
            locationClientOption = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}