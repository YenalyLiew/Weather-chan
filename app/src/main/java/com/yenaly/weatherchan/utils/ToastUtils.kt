package com.yenaly.weatherchan.utils

import android.widget.Toast
import com.yenaly.weatherchan.WeatherChanApplication

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/02/08 008 22:08
 * @Description : Toast的工具库。
 */

object ToastUtils {

    fun String.showShortToast() {
        Toast.makeText(WeatherChanApplication.context, this, Toast.LENGTH_SHORT).show()
    }

    fun String.showLongToast() {
        Toast.makeText(WeatherChanApplication.context, this, Toast.LENGTH_LONG).show()
    }

}