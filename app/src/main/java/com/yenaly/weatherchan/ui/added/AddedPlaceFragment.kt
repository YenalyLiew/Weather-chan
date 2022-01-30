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

class AddedPlaceFragment : Fragment() {

    val viewModelAdded by lazy { ViewModelProvider(requireActivity()).get(AddedPlaceViewModel::class.java) }
    val viewModelSearch by lazy { ViewModelProvider(requireActivity()).get(PlaceViewModel::class.java) }

    private var _binding: FragmentAddedPlaceBinding? = null
    private val binding get() = _binding!!
    //private lateinit var adapter: AddedPlaceAdapter

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

        val tipOne = "未添加任何地区"

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        if (viewModelAdded.isPlacesAdded())
            viewModelAdded.addPlacesInitialize()
        viewModelAdded.refreshPlaces()

        viewModelAdded.modifyLiveData.observe(viewLifecycleOwner) { placeList ->
            val adapter = AddedPlaceAdapter(this, placeList)
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