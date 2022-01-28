package com.yenaly.weatherchan.ui.weather

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yenaly.weatherchan.R
import com.yenaly.weatherchan.databinding.ActivityWeatherNewBinding
import com.yenaly.weatherchan.logic.model.Sky
import com.yenaly.weatherchan.logic.model.Weather
import com.yenaly.weatherchan.ui.weather.adapter.ViewPagerAdapter
import com.yenaly.weatherchan.ui.weather.viewmodel.WeatherViewModel

class WeatherActivity : AppCompatActivity(), DrawerLayout.DrawerListener {

    lateinit var binding: ActivityWeatherNewBinding
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_search)
        }

        if (viewModel.locationLng.isEmpty())
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        if (viewModel.locationLat.isEmpty())
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        if (viewModel.placeName.isEmpty())
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""

        //滑动冲突处理，只有在顶端时才能刷新
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            binding.swipeRefreshLayout.isEnabled = (verticalOffset >= 0)
        })

        binding.drawerLayout.addDrawerListener(this)

        viewModel.weatherLiveData.observe(this) { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeather(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
        refreshWeather()
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshWeather()
        }

    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun showWeather(weather: Weather) {
        binding.toolBar.title = ""
        binding.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        //
        val realtimeTemp = "${realtime.temperature} ℃"
        val realtimeSky = Sky.getSky(realtime.skycon).info
        val realtimeAirQuality = "空气质量：${realtime.airQuality.description.chn}"
        val realtimeFeelLikeTemp = "体感温度：${realtime.apparentTemperature} ℃"
        val realtimeLayoutBackground = Sky.getSky(realtime.skycon).bg
        binding.realtimeTemp.text = realtimeTemp
        binding.realtimeSky.text = realtimeSky
        binding.realtimeAirQuality.text = realtimeAirQuality
        binding.realtimeFeelLikeTemp.text = realtimeFeelLikeTemp
        Glide.with(this).load(realtimeLayoutBackground).into(binding.skyconImageView)
        //
        binding.viewPager.apply {
            adapter = ViewPagerAdapter(this@WeatherActivity)
            offscreenPageLimit = 2
            isUserInputEnabled = true
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                ViewPagerAdapter.PAGE_REALTIME -> tab.text = "今日"
                ViewPagerAdapter.PAGE_DAILY -> tab.text = "未来"
            }
        }.attach()
        binding.coordinatorLayout.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.city_added_list -> binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        return true
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

    override fun onDrawerOpened(drawerView: View) {}

    override fun onDrawerStateChanged(newState: Int) {}

    override fun onDrawerClosed(drawerView: View) {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}