package com.yenaly.weatherchan.ui.weather.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.weatherchan.R
import com.yenaly.weatherchan.logic.model.Sky
import com.yenaly.weatherchan.logic.model.Weather
import com.yenaly.weatherchan.ui.weather.fragment.DailyWeatherFragment
import java.text.SimpleDateFormat
import java.util.Locale

class DailyWeatherAdapter(
    private val fragment: DailyWeatherFragment,
    private val weather: Weather
) :
    RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateInfo = view.findViewById<TextView>(R.id.date_info)!!
        val skyIcon = view.findViewById<ImageView>(R.id.sky_icon)!!
        val skyInfo = view.findViewById<TextView>(R.id.sky_info)!!
        val tempInfo = view.findViewById<TextView>(R.id.temp_info)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_weather_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sky = Sky.getSky(weather.daily.skycon[position].value)
        val tempInfo =
            "${weather.daily.temperature[position].min} ~ ${weather.daily.temperature[position].max} ℃"
        holder.dateInfo.text = dateFormat.format(weather.daily.skycon[position].date)
        holder.skyIcon.setImageResource(sky.icon)
        holder.skyInfo.text = sky.info
        holder.tempInfo.text = tempInfo
        Log.d("DailyWeatherAdapter", position.toString())
    }

    override fun getItemCount(): Int {
        return weather.daily.skycon.size
    }
}