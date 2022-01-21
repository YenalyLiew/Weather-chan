package com.yenaly.weatherchan

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WeatherChanApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        // 建立全局Context
        lateinit var context: Context
        // 建立全局TOKEN
        const val TOKEN = "cAgKQqOVUXKQuJXC"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}