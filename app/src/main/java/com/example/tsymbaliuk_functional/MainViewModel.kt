package com.example.tsymbaliuk_functional

/*TODO: update list on database changes*/

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MainViewModel : ViewModel() {

    var listOfGeopoints = MutableLiveData<ArrayList<Geopoint>>()
    private val repository: AppRepository = AppRepository.getInstance()
    var currentGeopoint = MutableLiveData<Geopoint>()

    init {
        repository.geopointsLiveData.observeForever {
            listOfGeopoints.setValue(it)
        }
        currentGeopoint.value = Geopoint("","current location", LatLng(0.0, 0.0))
    }

    fun deleteGeopoint(documentName: String){
        repository.deleteGeopoint(documentName)
    }

}