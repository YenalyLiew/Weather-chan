package com.yenaly.weatherchan.ui.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.weatherchan.R
import com.yenaly.weatherchan.logic.model.Sky
import com.yenaly.weatherchan.logic.model.Weather
import com.yenaly.weatherchan.ui.weather.fragment.RealtimeDetailedFragment
import java.util.*

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/02/09 009 21:08
 * @Description : 当日天气中每小时天气的RecyclerView的适配器。
 */
class HourlyWeatherAdapter(
    private val fragment: RealtimeDetailedFragment,
    private val weather: Weather
) : RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>() {

    private val calendar = Calendar.getInstance()
    private val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hourlyHour = view.findViewById<TextView>(R.id.hourly_hour)!!
        val hourlyTemperature = view.findViewById<TextView>(R.id.hourly_temperature)!!
        val hourlySkyconImage = view.findViewById<ImageView>(R.id.hourly_skycon_image)!!
        val hourlyPrecipitationText = view.findViewById<TextView>(R.id.hourly_precipitation_text)!!
        val hourlyAqi = view.findViewById<TextView>(R.id.hourly_aqi)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_hourly_weather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unitOfTemperature = if (fragment.viewModel.getSettingsString(
                fragment.resources.getString(R.string.units),
                fragment.resources.getString(R.string.metric)
            ) == fragment.resources.getString(R.string.metric)
        ) "℃" else "℉"

        val nextHour = currentHour + position
        if (nextHour < 24) {
            val nextHourText = "${nextHour}:00"
            holder.hourlyHour.text = nextHourText
        } else {
            if (nextHour - 24 == 0)
                holder.hourlyHour.text = fragment.resources.getString(R.string.tomorrow)
            else {
                val nextHourText = "${nextHour - 24}:00"
                holder.hourlyHour.text = nextHourText
            }
        }

        val hourlyTemp =
            weather.hourly.temperature[position].value.toString() + " " + unitOfTemperature
        holder.hourlyTemperature.text = hourlyTemp

        val hourlyPrecipitation = weather.hourly.precipitation[position].value
        holder.hourlyPrecipitationText.text = hourlyPrecipitation

        val hourlyAqi = "AQI " + weather.hourly.airQuality.aqi[position].value.chn
        holder.hourlyAqi.text = hourlyAqi

        val sky = Sky.getSky(weather.hourly.skycon[position].value)
        holder.hourlySkyconImage.setImageResource(sky.icon)
    }

    override fun getItemCount(): Int {
        return weather.hourly.skycon.size
    }
}