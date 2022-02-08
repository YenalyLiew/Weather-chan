package com.yenaly.weatherchan.ui.place

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yenaly.weatherchan.R
import com.yenaly.weatherchan.databinding.FragmentPlaceSearchBinding
import com.yenaly.weatherchan.logic.model.PlaceResponse
import com.yenaly.weatherchan.ui.added.AddedPlaceViewModel
import com.yenaly.weatherchan.utils.ToastUtils.showShortToast
import com.yenaly.weatherchan.ui.weather.WeatherActivity

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 城市搜索的Fragment。
 */

class PlaceSearchFragment : Fragment() {

    val viewModelSearch by lazy { ViewModelProvider(requireActivity()).get(PlaceViewModel::class.java) }
    val viewModelCurrentPlace by lazy {
        ViewModelProvider(requireActivity()).get(CurrentPlaceViewModel::class.java)
    }
    val viewModelAdded by lazy { ViewModelProvider(requireActivity()).get(AddedPlaceViewModel::class.java) }
    private lateinit var adapter: PlaceSearchAdapter
    private var _binding: FragmentPlaceSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (viewModelSearch.isPlaceSaved() && activity !is WeatherActivity) {
            val savedPlace = viewModelSearch.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", savedPlace.location.lng)
                putExtra("location_lat", savedPlace.location.lat)
                putExtra("place_name", savedPlace.name)
            }
            startActivity(intent)
            activity?.finish()
        }
        _binding = FragmentPlaceSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        adapter = PlaceSearchAdapter(this, emptyList())

        //初始化adapter。
        viewModelSearch.refreshSearch()

        viewModelCurrentPlace.startLocation()

        binding.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModelSearch.placeList.clear()
                viewModelCurrentPlace.startLocation()
                addCurrentCityItem()
                viewModelSearch.searchPlaces(content)
            } else {
                binding.bgImageview.visibility = View.VISIBLE
                binding.searchTipText.visibility = View.GONE
                viewModelSearch.placeList.clear()
                viewModelCurrentPlace.startLocation()
                addCurrentCityItem()
                adapter.notifyDataSetChanged()
            }
        }

        viewModelSearch.placeLiveDataObserver.observe(viewLifecycleOwner) { result ->
            val places = result.getOrNull()
            if (!places.isNullOrEmpty() && places.isNotEmpty()) {
                binding.bgImageview.visibility = View.GONE
                binding.searchTipText.visibility = View.GONE

                //防止placeList在Activity重建时又重新add一遍。
                if (viewModelSearch.placeList.size == 1 || viewModelSearch.placeList.size == 0)
                    viewModelSearch.placeList.addAll(places)

                adapter.notifyDataSetChanged()
            } else {
                binding.searchTipText.visibility = View.VISIBLE
                binding.searchTipText.text = resources.getString(R.string.cannot_find_out_the_place)
                result.exceptionOrNull()?.printStackTrace()
            }
        }

        //动态刷新adapter。通过调用refreshSearch()使adapter实时刷新。
        //一般在其他Fragment对该Fragment内容产生影响时和初始化时使用。
        viewModelSearch.modifyLiveData.observe(viewLifecycleOwner) { placeList ->
            adapter = PlaceSearchAdapter(this, placeList)
            binding.recyclerView.adapter = adapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModelCurrentPlace.destroyLocation()
    }

    /**
     * 将搜索栏的第一位显示为通过[CurrentPlaceViewModel]获取到的地区信息。
     */
    private fun addCurrentCityItem() {
        if (isCurrentCityItemAdded()) {
            viewModelSearch.placeList.add(
                0, PlaceResponse.Place(
                    viewModelCurrentPlace.currentCity,
                    PlaceResponse.Place.Location(
                        viewModelCurrentPlace.currentLng,
                        viewModelCurrentPlace.currentLat
                    ),
                    viewModelCurrentPlace.currentProvince
                )
            )
        }
    }

    /**
     * 判断第一栏是否为通过[CurrentPlaceViewModel]获取到的地区信息。
     */
    private fun isCurrentCityItemAdded(): Boolean {
        return viewModelCurrentPlace.currentCity.isNotEmpty() &&
                viewModelCurrentPlace.currentLng.isNotEmpty() &&
                viewModelCurrentPlace.currentLat.isNotEmpty() &&
                viewModelCurrentPlace.currentProvince.isNotEmpty() &&
                viewModelSearch.placeList.isEmpty()
    }

    /** 初始化当前位置信息 */
    @SuppressLint("NotifyDataSetChanged")
    private fun initLocation() {
        viewModelCurrentPlace.getOnceLocation { location ->
            if (location != null) {
                when (location.errorCode) {
                    0 -> {
                        viewModelCurrentPlace.currentLng = location.longitude.toString()
                        viewModelCurrentPlace.currentLat = location.latitude.toString()
                        viewModelCurrentPlace.currentCity = location.district
                        viewModelCurrentPlace.currentProvince =
                            "(${resources.getString(R.string.current_location)}) ${location.country} ${location.province} ${location.city}"
                        addCurrentCityItem()
                        adapter.notifyDataSetChanged()
                    }
                    9 -> resources.getString(R.string.amap_location_error_9).showShortToast()
                    12 -> resources.getString(R.string.amap_location_error_12).showShortToast()
                    13 -> resources.getString(R.string.amap_location_error_13).showShortToast()
                    else -> resources.getString(R.string.amap_location_error_else).showShortToast()
                }
            }
        }
    }

}