package com.example.tsymbaliuk_functional

import com.google.android.gms.maps.GoogleMap
import org.jetbrains.annotations.NotNull


object GoogleMapHelper {
    fun defaultMapSettings(@NotNull googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isTiltGesturesEnabled = true
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.isBuildingsEnabled = true
    }
}