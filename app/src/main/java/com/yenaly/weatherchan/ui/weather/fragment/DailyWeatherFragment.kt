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

object DailyWeatherFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java) }
    private lateinit var weather: Weather
    private var binding_: ForecastWeatherBinding? = null
    private val binding get() = binding_!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = ForecastWeatherBinding.inflate(inflater, container, false)
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
        val layoutManager = LinearLayoutManager(activity)
        binding.forecastItemRv.layoutManager = layoutManager
        val adapter = DailyWeatherAdapter(this, weather)
        binding.forecastItemRv.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
}