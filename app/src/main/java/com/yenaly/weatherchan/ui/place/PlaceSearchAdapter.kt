package com.yenaly.weatherchan.ui.place

import android.content.Intent
import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.yenaly.weatherchan.R
import com.yenaly.weatherchan.logic.model.PlaceResponse
import com.yenaly.weatherchan.ui.weather.WeatherActivity

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 城市搜索Fragment的RecyclerView的适配器。
 */

class PlaceSearchAdapter(
    private val fragment: PlaceSearchFragment,
    private val placeList: List<PlaceResponse.Place>
) : RecyclerView.Adapter<PlaceSearchAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName = view.findViewById<TextView>(R.id.place_name)!!
        val placeAddress = view.findViewById<TextView>(R.id.place_address)!!
        val itemPlaceCardView = view.findViewById<MaterialCardView>(R.id.item_place_card_view)!!
        val addButton = view.findViewById<Button>(R.id.add_button)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_place_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        val activity = fragment.activity

        //搜索栏第一个IP定位地区的Card颜色加深并且取消添加按钮。
        if (position == 0 &&
            fragment.viewModelIP.currentCity.isNotEmpty() &&
            fragment.viewModelIP.currentLng.isNotEmpty() &&
            fragment.viewModelIP.currentLat.isNotEmpty() &&
            fragment.viewModelIP.currentProvince.isNotEmpty()
        ) {

            //判断当前是否为夜间模式
            val currentMode =
                fragment.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (currentMode != Configuration.UI_MODE_NIGHT_YES)
                holder.itemPlaceCardView.setCardBackgroundColor(0xFFF5F5F5.toInt())
            else
                holder.itemPlaceCardView.setCardBackgroundColor(0xFF281F1D.toInt())

            holder.addButton.visibility = View.GONE
        }

        holder.addButton.setOnClickListener {
            it.isHapticFeedbackEnabled = true
            it.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            fragment.viewModelAdded.addPlace(place)
            if (fragment.viewModelAdded.isPlaceAdded(place)) {
                Toast.makeText(fragment.context, "添加成功", Toast.LENGTH_SHORT).show()
                holder.addButton.visibility = View.GONE
                fragment.viewModelAdded.refreshPlaces()
            }
        }
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
        holder.itemView.setOnClickListener {
            fragment.viewModelSearch.savePlace(place)
            if (activity is WeatherActivity) {
                activity.binding.drawerLayout.closeDrawers()
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat = place.location.lat
                activity.viewModel.placeName = place.name
                activity.refreshWeather()
            } else {
                val intent = Intent(fragment.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
                fragment.activity?.finish()
            }
        }

        //若首次打开程序选择城市时，隐藏添加按钮，暂时不提供添加功能。
        if (activity !is WeatherActivity)
            holder.addButton.visibility = View.GONE

        //若地区已被添加，将按钮隐藏。
        if (fragment.viewModelAdded.isPlaceAdded(place))
            holder.addButton.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

}