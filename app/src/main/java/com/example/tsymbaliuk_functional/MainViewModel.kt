package com.example.tsymbaliuk_functional

/*TODO: update list on database changes*/
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var listOfGeopoints = MutableLiveData<ArrayList<Geopoint>>()
    private val repository: AppRepository = AppRepository.getInstance()

    init {
        repository.geopointsLiveData.observeForever {
            listOfGeopoints.setValue(it)
        }
    }

    fun deleteGeopoint(documentName: String){
        repository.deleteGeopoint(documentName)
    }

}