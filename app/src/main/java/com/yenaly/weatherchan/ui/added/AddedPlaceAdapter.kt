package com.yenaly.weatherchan.ui.added

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.weatherchan.R
import com.yenaly.weatherchan.logic.model.PlaceResponse
import com.yenaly.weatherchan.ui.weather.WeatherActivity

class AddedPlaceAdapter(
    private val fragment: AddedPlaceFragment,
    private val listAddedPlace: List<PlaceResponse.Place>
) :
    RecyclerView.Adapter<AddedPlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addedPlaceName = view.findViewById<TextView>(R.id.added_place_name)!!
        val addedPlaceAddress = view.findViewById<TextView>(R.id.added_place_address)!!
        val deleteButton = view.findViewById<Button>(R.id.delete_button)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_added_place, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.addedPlaceName.text = listAddedPlace[position].name
        holder.addedPlaceAddress.text = listAddedPlace[position].address
        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(fragment.requireContext())
                .setTitle("提示")
                .setMessage("确定要将 " + listAddedPlace[position].name + " 移除添加吗？")
                .setPositiveButton("是") { _, _ ->
                    fragment.viewModelAdded.deletePlace(listAddedPlace[position])
                    notifyItemRemoved(position)
                    if (position != listAddedPlace.size) {
                        notifyItemRangeChanged(position, listAddedPlace.size - position)
                    }
                    if (listAddedPlace.isEmpty()) fragment.viewModelAdded.refreshPlaces()
                    fragment.viewModelSearch.refreshSearch()
                }
                .setNegativeButton("取消") { _, _ -> }
                .show()

        }
        holder.itemView.setOnClickListener {
            val activity = fragment.activity as WeatherActivity
            activity.binding.drawerLayout.closeDrawers()
            fragment.viewModelSearch.savePlace(listAddedPlace[position])
            activity.viewModel.locationLng = listAddedPlace[position].location.lng
            activity.viewModel.locationLat = listAddedPlace[position].location.lat
            activity.viewModel.placeName = listAddedPlace[position].name
            activity.refreshWeather()
        }
    }

    override fun getItemCount(): Int {
        return listAddedPlace.size
    }
}