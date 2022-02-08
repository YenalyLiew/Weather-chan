package com.yenaly.weatherchan.ui.weather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.yenaly.weatherchan.ui.settings.SettingsActivity
import com.yenaly.weatherchan.ui.weather.adapter.ViewPagerAdapter
import com.yenaly.weatherchan.ui.weather.viewmodel.WeatherViewModel

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 天气的Activity。
 */

class WeatherActivity : AppCompatActivity(), DrawerLayout.DrawerListener {

    lateinit var binding: ActivityWeatherNewBinding
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    private val handler = Handler(Looper.getMainLooper())
    private val runnableCycleRefresh = object : Runnable {
        override fun run() {
            //设置2分钟一刷新
            handler.postDelayed(this, 2 * 60 * 1000)
            refreshWeather()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeActionContentDescription("Search")
            it.setHomeAsUpIndicator(R.drawable.ic_search)
        }

        getMetricOrImperial()

        if (viewModel.locationLng.isEmpty())
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        if (viewModel.locationLat.isEmpty())
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        if (viewModel.placeName.isEmpty())
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""

        refreshWeather()

        //滑动冲突处理，只有在顶端时才能刷新。
        binding.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshWeather()
        }

        //启动定时器。
        handler.post(runnableCycleRefresh)

    }

    /**
     * 刷新当前天气。
     */
    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefreshLayout.isRefreshing = true
    }

    /**
     * 显示当前天气。
     */
    private fun showWeather(weather: Weather) {

        //隐藏ToolBar的title。ToolBar居中显示城市名称。
        binding.toolBar.title = ""
        binding.placeName.text = viewModel.placeName

        val unitOfTemperature = if (viewModel.getSettingsString(
                resources.getString(R.string.units),
                resources.getString(R.string.metric)
            ) == resources.getString(R.string.metric)
        ) "℃" else "℉"
        val realtime = weather.realtime
        val realtimeTemp = "${realtime.temperature}" + " " + unitOfTemperature
        val realtimeSky = Sky.getSky(realtime.skycon).info
        val realtimeAirQuality =
            resources.getString(R.string.air_quality) + ": " + realtime.airQuality.description.chn
        val realtimeFeelLikeTemp =
            resources.getString(R.string.feel_like_temperature) + ": " + "${realtime.apparentTemperature}" + " " + unitOfTemperature
        val realtimeLayoutBackground = Sky.getSky(realtime.skycon).bg
        binding.realtimeTemp.text = realtimeTemp
        binding.realtimeSky.text = realtimeSky
        binding.realtimeAirQuality.text = realtimeAirQuality
        binding.realtimeFeelLikeTemp.text = realtimeFeelLikeTemp
        Glide.with(this).load(realtimeLayoutBackground).into(binding.skyconImageView)
        binding.viewPager.apply {
            adapter = ViewPagerAdapter(this@WeatherActivity)
            offscreenPageLimit = 2
            isUserInputEnabled = true
        }

        //TabLayout和ViewPager2进行绑定。
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                ViewPagerAdapter.PAGE_REALTIME -> tab.text = resources.getString(R.string.today)
                ViewPagerAdapter.PAGE_DAILY -> tab.text = resources.getString(R.string.future)
            }
        }.attach()

        binding.coordinatorLayout.visibility = View.VISIBLE
    }

    /**
     * 用来储存（可能）修改前的`unit`。
     *
     * `SharedPreferences.OnSharedPreferenceChangeListener`
     * 中监听设置内容是否修改的粗略实现。
     */
    private var unitCache: String? = null

    override fun onPause() {
        super.onPause()

        //获取（可能）修改前的单位。
        unitCache = viewModel.unit
    }

    override fun onResume() {
        super.onResume()
        getMetricOrImperial()

        //若有修改则刷新，反之不刷新。
        if (unitCache != viewModel.unit)
            refreshWeather()
    }

    /**
     * 获取当前是英制单位或公制单位。
     */
    private fun getMetricOrImperial() {
        val unit = viewModel.getSettingsString(
            resources.getString(R.string.units),
            resources.getString(R.string.metric)
        )

        //若有修改则赋值，反之不赋值。
        if (unit != unitCache)
            unit?.let {
                if (it == resources.getString(R.string.metric))
                    viewModel.unit = "metric"
                else
                    viewModel.unit = "imperial"
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.city_added_list -> binding.drawerLayout.openDrawer(GravityCompat.END)
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        //关闭定时器。
        handler.removeCallbacks(runnableCycleRefresh)
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }

    override fun onDrawerOpened(drawerView: View) {
    }

    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun onDrawerClosed(drawerView: View) {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}