package com.yenaly.weatherchan.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yenaly.weatherchan.logic.Repository
import com.yenaly.weatherchan.logic.model.IPWithCityAndProvince

class CurrentIpViewModel : ViewModel() {

    var currentCity = ""
    var currentProvince = ""
    var currentLng = ""
    var currentLat = ""

    private val ipLiveData = MutableLiveData<IPWithCityAndProvince>()
    val currentIPLiveData = Transformations.switchMap(ipLiveData) {
        Repository.getCurrentIP()
    }

    fun getCurrentIP() {
        ipLiveData.value = ipLiveData.value
    }
}