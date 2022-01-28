package com.yenaly.weatherchan.ui.weather.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yenaly.weatherchan.databinding.RealtimeWeatherDetailedBinding
import com.yenaly.weatherchan.logic.model.Weather
import com.yenaly.weatherchan.ui.weather.viewmodel.WeatherViewModel

object RealtimeDetailedFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java) }
    private lateinit var weather: Weather
    private var _binding: RealtimeWeatherDetailedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RealtimeWeatherDetailedBinding.inflate(inflater, container, false)
        viewModel.weatherLiveData.observe(requireActivity()) { result ->
            if (result.getOrNull() != null) {
                weather = result.getOrNull()!!
            } else {
                Log.d("RealtimeDetailedFrag", "weatherLiveData's result got null!!")
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val humidityText = weather.realtime.humidity
        val precipitationText = weather.realtime.precipitation.local.intensity
        val cloudrateText = weather.realtime.cloudrate
        val pressureText = weather.realtime.pressure
        val visibilityText = weather.realtime.visibility
        val windText = "${weather.realtime.wind.direction} / ${weather.realtime.wind.speed}"
        val carWashingText = weather.daily.lifeIndex.carWashing[0].desc
        val coldRiskText = weather.daily.lifeIndex.coldRisk[0].desc
        val dressingText = weather.daily.lifeIndex.dressing[0].desc
        val uvText = weather.daily.lifeIndex.ultraviolet[0].desc
        binding.realtimeWeatherIndexLayout.humidityText.text = humidityText.toString()
        binding.realtimeWeatherIndexLayout.precipitationText.text = precipitationText.toString()
        binding.realtimeWeatherIndexLayout.cloudrateText.text = cloudrateText.toString()
        binding.realtimeWeatherIndexLayout.pressureText.text = pressureText.toString()
        binding.realtimeWeatherIndexLayout.visibilityText.text = visibilityText.toString()
        binding.realtimeWeatherIndexLayout.windText.text = windText
        binding.realtimeWeatherLifeIndexLayout.carWashingText.text = carWashingText
        binding.realtimeWeatherLifeIndexLayout.coldRiskText.text = coldRiskText
        binding.realtimeWeatherLifeIndexLayout.dressingText.text = dressingText
        binding.realtimeWeatherLifeIndexLayout.uvText.text = uvText
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}