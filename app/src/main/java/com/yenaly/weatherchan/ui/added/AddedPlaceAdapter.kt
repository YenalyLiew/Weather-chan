package com.yenaly.weatherchan.ui.added

import android.annotation.SuppressLint
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.weatherchan.R
import com.yenaly.weatherchan.logic.model.PlaceResponse
import com.yenaly.weatherchan.utils.TextViewUtils
import com.yenaly.weatherchan.ui.weather.WeatherActivity

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : [AddedPlaceFragment]里的[RecyclerView]的适配器。
 */

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
        holder.addedPlaceName.apply {
            text = listAddedPlace[position].name
            TextViewUtils.makeTextMarquee(this)
        }
        holder.addedPlaceAddress.text = listAddedPlace[position].address
        holder.deleteButton.setOnClickListener {
            it.isHapticFeedbackEnabled = true
            it.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            AlertDialog.Builder(fragment.requireContext())
                .setTitle("提示")
                .setMessage("确定要将 " + listAddedPlace[position].name + " 移除添加吗？")
                .setPositiveButton("是") { _, _ ->
                    fragment.viewModelAdded.deletePlace(listAddedPlace[position])

                    //保证删除位置及动画不发生错乱。
                    notifyItemRemoved(position)
                    if (position != listAddedPlace.size) {
                        notifyItemRangeChanged(position, listAddedPlace.size - position)
                    }

                    //当list删除最后一个item后，添加位置fragment进行刷新，保证界面显示正确。
                    if (listAddedPlace.isEmpty()) fragment.viewModelAdded.refreshPlaces()

                    //地区搜索Fragment进行刷新，保证添加icon的正常显示或隐藏。
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