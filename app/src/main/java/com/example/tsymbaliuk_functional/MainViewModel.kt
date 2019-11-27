package com.example.tsymbaliuk_functional

/*TODO: update list on database changes*/
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var listOfGeopoints = MutableLiveData<ArrayList<Geopoint>>()
    private val repository: AppRepository = AppRepository.getInstance()
    var currentGeopoint = MutableLiveData<Geopoint>()

    init {
        repository.geopointsLiveData.observeForever {
            listOfGeopoints.setValue(it)
        }
        currentGeopoint.value = Geopoint("current location", 0.0, 0.0)
    }

    fun deleteGeopoint(documentName: String){
        repository.deleteGeopoint(documentName)
    }

}