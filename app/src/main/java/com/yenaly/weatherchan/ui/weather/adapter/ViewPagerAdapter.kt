package com.yenaly.weatherchan.ui.weather.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yenaly.weatherchan.ui.weather.fragment.DailyWeatherFragment
import com.yenaly.weatherchan.ui.weather.fragment.EmptyFragment
import com.yenaly.weatherchan.ui.weather.fragment.RealtimeDetailedFragment

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : WeatherActivity里的ViewPager2的适配器。
 */

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    companion object {
        const val PAGE_REALTIME = 0
        const val PAGE_DAILY = 1
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_REALTIME -> RealtimeDetailedFragment.newInstance()
            PAGE_DAILY -> DailyWeatherFragment.newInstance()
            else -> EmptyFragment.newInstance()
        }
    }
}