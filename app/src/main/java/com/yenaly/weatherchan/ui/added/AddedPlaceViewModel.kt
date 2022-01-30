package com.yenaly.weatherchan.ui.added

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yenaly.weatherchan.logic.Repository
import com.yenaly.weatherchan.logic.model.PlaceResponse

class AddedPlaceViewModel : ViewModel() {
    private val addedPlaces = ArrayList<PlaceResponse.Place>()
    val modifyLiveData = MutableLiveData<List<PlaceResponse.Place>>()

    private fun addPlaces() = Repository.addPlaces(addedPlaces)

    fun refreshPlaces() {
        modifyLiveData.value = addedPlaces
    }

    fun addPlace(place: PlaceResponse.Place) {
        addedPlaces.add(place)
        addPlaces()
    }

    fun deletePlace(place: PlaceResponse.Place) {
        addedPlaces.remove(place)
        addPlaces()
    }

    fun addPlacesInitialize() = addedPlaces.addAll(getAddedPlaces())

    private fun getAddedPlaces() = Repository.getAddedPlaces()

    fun isListNotEmpty() = addedPlaces.isNotEmpty()

    fun isPlaceAdded(place: PlaceResponse.Place, places: List<PlaceResponse.Place> = addedPlaces) =
        Repository.isPlaceAdded(place, places)

    fun isPlacesAdded() = Repository.isPlacesAdded()
}