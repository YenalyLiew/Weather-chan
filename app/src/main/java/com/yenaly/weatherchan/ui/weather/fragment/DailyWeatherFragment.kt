package com.yenaly.weatherchan.ui.weather.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yenaly.weatherchan.databinding.ForecastWeatherBinding
import com.yenaly.weatherchan.logic.model.Weather
import com.yenaly.weatherchan.ui.weather.viewmodel.WeatherViewModel
import com.yenaly.weatherchan.ui.weather.adapter.DailyWeatherAdapter

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 未来天气的Fragment。
 */

class DailyWeatherFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java) }
    private lateinit var weather: Weather
    private var _binding: ForecastWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ForecastWeatherBinding.inflate(inflater, container, false)
        viewModel.weatherLiveData.observe(requireActivity()) { result ->
            if (result.getOrNull() != null) {
                weather = result.getOrNull()!!
            } else {
                Log.d("DailyWeatherFrag", "weatherLiveData's result got null!!")
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //在Activity刷新后读取之前保存过的内容。
        if (savedInstanceState != null) {
            weather = savedInstanceState.getSerializable("daily_weather") as Weather
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.forecastItemRv.layoutManager = layoutManager
        val adapter = DailyWeatherAdapter(this, weather)
        binding.forecastItemRv.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("daily_weather", weather)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = DailyWeatherFragment()
    }

}