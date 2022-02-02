package com.yenaly.weatherchan.ui.added

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yenaly.weatherchan.databinding.FragmentAddedPlaceBinding
import com.yenaly.weatherchan.ui.place.PlaceViewModel

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/1/28 15:53
 * @Description : 添加城市的Fragment。
 */

class AddedPlaceFragment : Fragment() {

    val viewModelAdded by lazy { ViewModelProvider(requireActivity()).get(AddedPlaceViewModel::class.java) }
    val viewModelSearch by lazy { ViewModelProvider(requireActivity()).get(PlaceViewModel::class.java) }
    private lateinit var adapter: AddedPlaceAdapter
    private var _binding: FragmentAddedPlaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddedPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //错误信息以及问题信息。
        val tipOne = "未添加任何地区"

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        adapter = AddedPlaceAdapter(this, emptyList())

        if (viewModelAdded.isPlacesAdded() && !viewModelAdded.containsAddedPlaces())
            viewModelAdded.addPlacesInitialize()
        viewModelAdded.refreshPlaces()

        //动态刷新adapter。通过调用refreshPlaces()使adapter实时刷新。
        //一般在其他Fragment对该Fragment内容产生影响时和初始化时使用。
        viewModelAdded.modifyLiveData.observe(viewLifecycleOwner) { placeList ->
            adapter = AddedPlaceAdapter(this, placeList)
            binding.recyclerView.adapter = adapter
            if (viewModelAdded.isListNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.addedTipText.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.addedTipText.text = tipOne
                binding.addedTipText.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}