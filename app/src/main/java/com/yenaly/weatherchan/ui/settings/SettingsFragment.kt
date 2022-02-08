package com.yenaly.weatherchan.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.yenaly.weatherchan.R

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/02/06 006 11:57
 * @Description : 设置的Fragment。
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}