package com.example.tsymbaliuk_functional

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.ArrayList


class AppRepository {

    private val geopoints = ArrayList<Geopoint>()
    val geopointsLiveData = MutableLiveData<ArrayList<Geopoint>>()
    val application = MyApplication.instance

    companion object {

        val TAG = "AppRepository"

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: AppRepository().also { instance = it }
            }
    }

    init {
        observeGeopoints()
    }

    fun observeGeopoints(): MutableLiveData<ArrayList<Geopoint>> {
        geopoints.clear()
        application.database.collection("city_coordinates_ua")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    snapshots: QuerySnapshot?,
                    exception: FirebaseFirestoreException?
                ) {
                    if (exception != null) {
                        Log.w(TAG, "Listen failed.", exception)
                        return
                    }
                    if (snapshots != null) {
                        geopoints.clear()
                        for (doc in snapshots) {
                            if (doc.exists()) {
                                val geoPoint = doc.getGeoPoint("geopoint")
                                val geopoint =
                                    Geopoint(doc.id, geoPoint!!.latitude, geoPoint.longitude)
                                geopoints.add(geopoint)
                            }
                        }
                        if (geopoints.isNotEmpty()) {
                        geopoints.sortedWith(compareBy { it.name?.toLowerCase(Locale.ROOT) }) }
                        geopointsLiveData.value = geopoints
                    }
                }
            })
        return geopointsLiveData
    }


    fun deleteGeopoint(documentName: String) {
        application.database.collection("city_coordinates_ua").document(documentName)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

}