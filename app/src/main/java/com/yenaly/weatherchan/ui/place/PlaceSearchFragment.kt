package com.yenaly.weatherchan.ui.place

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.yenaly.weatherchan.WeatherChanApplication
import com.yenaly.weatherchan.databinding.FragmentPlaceSearchBinding
import com.yenaly.weatherchan.logic.model.PlaceResponse
import com.yenaly.weatherchan.ui.added.AddedPlaceViewModel
import com.yenaly.weatherchan.ui.weather.WeatherActivity
import java.lang.Exception

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 城市搜索的Fragment。
 */

class PlaceSearchFragment : Fragment() {

    val viewModelSearch by lazy { ViewModelProvider(requireActivity()).get(PlaceViewModel::class.java) }
    val viewModelIP by lazy { ViewModelProvider(requireActivity()).get(CurrentIpViewModel::class.java) }
    val viewModelAdded by lazy { ViewModelProvider(requireActivity()).get(AddedPlaceViewModel::class.java) }
    private lateinit var adapter: PlaceSearchAdapter
    private var locationClient: AMapLocationClient? = null
    private var locationClientOption: AMapLocationClientOption? = null
    private var _binding: FragmentPlaceSearchBinding? = null
    private val binding get() = _binding!!

    companion object {
        //错误信息以及问题信息。
        private const val tipNO = "未能查询到该地点"
        private const val tipNINE = "定位初始化时出现异常。\n请重新启动定位。"
        private const val tipTWELVE = "缺少定位权限。\n请在设备的设置中开启app的定位权限。"
        private const val tipTHIRTEEN = "定位失败，由于未获得WIFI列表和基站信息，且GPS当前不可用。\n建议检查权限是否开启。"
        private const val tipELSE = "定位出现异常，请稍后再试。"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AMapLocationClient.updatePrivacyAgree(WeatherChanApplication.context, true)
        AMapLocationClient.updatePrivacyShow(WeatherChanApplication.context, true, true)
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTPS)
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

        startLocation()

        binding.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModelSearch.placeList.clear()
                startLocation()
                addCurrentCityItem()
                viewModelSearch.searchPlaces(content)
            } else {
                binding.bgImageview.visibility = View.VISIBLE
                binding.searchTipText.visibility = View.GONE
                viewModelSearch.placeList.clear()
                startLocation()
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
                binding.searchTipText.text = tipNO
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
        destroyLocation()
    }

    /**
     * 将搜索栏的第一位显示为通过[CurrentIpViewModel]获取到的地区信息。
     */
    private fun addCurrentCityItem() {
        if (isCurrentCityItemAdded()) {
            viewModelSearch.placeList.add(
                0, PlaceResponse.Place(
                    viewModelIP.currentCity,
                    PlaceResponse.Place.Location(
                        viewModelIP.currentLng,
                        viewModelIP.currentLat
                    ),
                    viewModelIP.currentProvince
                )
            )
        }
    }

    /**
     * 判断第一栏是否为通过[CurrentIpViewModel]获取到的地区信息。
     */
    private fun isCurrentCityItemAdded(): Boolean {
        return viewModelIP.currentCity.isNotEmpty() &&
                viewModelIP.currentLng.isNotEmpty() &&
                viewModelIP.currentLat.isNotEmpty() &&
                viewModelIP.currentProvince.isNotEmpty() &&
                viewModelSearch.placeList.isEmpty()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initLocation() {
        try {
            locationClient = AMapLocationClient(WeatherChanApplication.context)
            locationClientOption = AMapLocationClientOption()
            locationClientOption?.isOnceLocation = true
            locationClient?.setLocationOption(locationClientOption)
            locationClient?.setLocationListener { location ->
                if (location != null) {
                    when (location.errorCode) {
                        0 -> {
                            viewModelIP.currentLng = location.longitude.toString()
                            viewModelIP.currentLat = location.latitude.toString()
                            viewModelIP.currentCity = location.district
                            viewModelIP.currentProvince =
                                "(当前定位) ${location.country} ${location.province} ${location.city}"
                            addCurrentCityItem()
                            adapter.notifyDataSetChanged()
                        }
                        9 -> tipNINE.toast()
                        12 -> tipTWELVE.toast()
                        13 -> tipTHIRTEEN.toast()
                        else -> tipELSE.toast()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startLocation() {
        try {
            locationClient?.startLocation()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun destroyLocation() {
        try {
            locationClient?.onDestroy()
            locationClient = null
            locationClientOption = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun String.toast() = Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
}