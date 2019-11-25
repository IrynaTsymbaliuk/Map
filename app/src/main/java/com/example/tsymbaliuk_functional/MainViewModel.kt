package com.example.tsymbaliuk_functional

/*TODO: update list on database changes*/
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var listOfGeopoints = MutableLiveData<ArrayList<Geopoint>>()

    init {
        getGeopoints()
    }

    fun getGeopoints(): MutableLiveData<ArrayList<Geopoint>> {
        MyApplication.instance.database.collection("city_coordinates_ua")
            .get()
            .addOnSuccessListener { result ->
                listOfGeopoints.value?.clear()
                for (document in result) {
                    if (document.getGeoPoint("geopoint") != null) {
                        val geoPoint = document.getGeoPoint("geopoint")
                        val geopoint =
                            Geopoint(document.id, geoPoint!!.latitude, geoPoint.longitude)
                        listOfGeopoints.value?.add(geopoint)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("DATABASE", "Error getting documents.", exception)
            }
        return listOfGeopoints
    }

}