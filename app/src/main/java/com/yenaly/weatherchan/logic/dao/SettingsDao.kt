package com.yenaly.weatherchan.logic.dao

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.yenaly.weatherchan.WeatherChanApplication

/**
 * @ProjectName : Weather-chan
 * @Author : 作者
 * @Time : 2022/02/06 006 18:58
 * @Description : 文件描述
 */
object SettingsDao {

    fun getSettingsString(string: String, defValue: String): String? {
        return sharedPreferences().getString(string, defValue)
    }

    private fun sharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(WeatherChanApplication.context)
    }

}