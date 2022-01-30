package com.yenaly.weatherchan.ui.place

import android.content.Intent
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
        if (position == 0 &&
            fragment.viewModelIP.currentCity.isNotEmpty() &&
            fragment.viewModelIP.currentLng.isNotEmpty() &&
            fragment.viewModelIP.currentLat.isNotEmpty() &&
            fragment.viewModelIP.currentProvince.isNotEmpty()
        ) {
            holder.itemPlaceCardView.setCardBackgroundColor(0xFFF5F5F5.toInt())
            holder.addButton.visibility = View.GONE
        }
        holder.addButton.setOnClickListener {
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

        if (activity !is WeatherActivity)
            holder.addButton.visibility = View.GONE

        if (fragment.viewModelAdded.isPlaceAdded(place))
            holder.addButton.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

}