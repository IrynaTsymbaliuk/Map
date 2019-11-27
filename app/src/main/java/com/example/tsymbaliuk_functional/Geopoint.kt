package com.example.tsymbaliuk_functional

import com.google.maps.android.clustering.ClusterItem
import com.google.android.gms.maps.model.LatLng

class Geopoint(var id: String, var name: String = "",  var latLng: LatLng = LatLng(0.0, 0.0)) : ClusterItem {



    override fun getSnippet(): String {
        return ""
    }

    override fun getTitle(): String {
        return name
    }

    override fun getPosition(): LatLng {
        return latLng
    }

}