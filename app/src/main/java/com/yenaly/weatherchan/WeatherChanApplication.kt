package com.yenaly.weatherchan

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WeatherChanApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        // 建立全局Context
        lateinit var context: Context
        // 建立全局TOKEN，彩云天气API
        const val TOKEN = "cAgKQqOVUXKQuJXC"
        // 建立全局KEY，高德API
        const val KEY = "ec0fab8dc40532806c5126c19112a34c"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}