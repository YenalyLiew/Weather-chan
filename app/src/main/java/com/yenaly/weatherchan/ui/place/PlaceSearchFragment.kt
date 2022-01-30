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
import com.yenaly.weatherchan.databinding.FragmentPlaceSearchBinding
import com.yenaly.weatherchan.logic.model.PlaceResponse
import com.yenaly.weatherchan.ui.added.AddedPlaceViewModel
import com.yenaly.weatherchan.ui.weather.WeatherActivity

class PlaceSearchFragment : Fragment() {

    val viewModelSearch by lazy { ViewModelProvider(requireActivity()).get(PlaceViewModel::class.java) }
    val viewModelIP by lazy { ViewModelProvider(requireActivity()).get(CurrentIpViewModel::class.java) }
    val viewModelAdded by lazy { ViewModelProvider(requireActivity()).get(AddedPlaceViewModel::class.java) }
    private lateinit var adapter: PlaceSearchAdapter
    private var _binding: FragmentPlaceSearchBinding? = null
    private val binding get() = _binding!!

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
        viewModelSearch.refreshSearch()

        val tipOne = "未能查询到该地点"

        //激活CurrentIpViewModel
        viewModelIP.getCurrentIP()

        binding.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModelSearch.placeList.clear()
                addCurrentCityItem()
                viewModelSearch.searchPlaces(content)
            } else {
                binding.bgImageview.visibility = View.VISIBLE
                binding.searchTipText.visibility = View.GONE
                viewModelSearch.placeList.clear()
                addCurrentCityItem()
                adapter.notifyDataSetChanged()
            }
        }

        viewModelSearch.placeLiveDataObserver.observe(viewLifecycleOwner) { result ->
            val places = result.getOrNull()
            if (!places.isNullOrEmpty() && places.isNotEmpty()) {
                binding.bgImageview.visibility = View.GONE
                binding.searchTipText.visibility = View.GONE
                viewModelSearch.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                binding.searchTipText.visibility = View.VISIBLE
                binding.searchTipText.text = tipOne
                result.exceptionOrNull()?.printStackTrace()
            }
        }

        viewModelSearch.modifyLiveData.observe(viewLifecycleOwner) { placeList ->
            adapter = PlaceSearchAdapter(this, placeList)
            binding.recyclerView.adapter = adapter
        }

        viewModelIP.currentIPLiveData.observe(viewLifecycleOwner) { result ->
            val iCP = result.getOrNull()
            if (iCP != null) {
                viewModelIP.currentCity = iCP.city
                viewModelIP.currentProvince = "(当前定位) " + iCP.province
                val currentLngAndLat = iCP.lngandlat.split(",")
                viewModelIP.currentLng = currentLngAndLat[0]
                viewModelIP.currentLat = currentLngAndLat[1]
            } else {
                viewModelIP.currentCity = ""
                viewModelIP.currentProvince = ""
                viewModelIP.currentLng = ""
                viewModelIP.currentLat = ""
            }
            addCurrentCityItem()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addCurrentCityItem() {
        if (viewModelIP.currentCity.isNotEmpty() &&
            viewModelIP.currentLng.isNotEmpty() &&
            viewModelIP.currentLat.isNotEmpty()
            && viewModelIP.currentProvince.isNotEmpty()
        ) {
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

}