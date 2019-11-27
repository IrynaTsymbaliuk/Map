package com.example.tsymbaliuk_functional

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator


@SuppressLint("InflateParams")
class MarkerClusterRenderer(
    context: Context,
    private val googleMap: GoogleMap,
    clusterManager: ClusterManager<Geopoint>,
    val geopoint: Geopoint
) :
    DefaultClusterRenderer<Geopoint>(context, googleMap, clusterManager),
    OnClusterClickListener<Geopoint>, OnInfoWindowClickListener {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val clusterIconGenerator: IconGenerator
    private val clusterItemView: View

    override fun onBeforeClusterItemRendered(
        item: Geopoint,
        markerOptions: MarkerOptions
    ) {
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        markerOptions.title(item.title)
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<Geopoint>,
        markerOptions: MarkerOptions
    ) {
        val singleClusterMarkerSizeTextView =
            clusterItemView.findViewById<TextView>(R.id.singleClusterMarkerSizeTextView)
        singleClusterMarkerSizeTextView.text = cluster.size.toString()
        val icon = clusterIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onClusterItemRendered(
        clusterItem: Geopoint,
        marker: Marker
    ) {
        marker.tag = clusterItem
        if (marker.title == geopoint.name) {
            marker.showInfoWindow()
        }
    }

    override fun onClusterClick(cluster: Cluster<Geopoint>): Boolean {
        val builder = LatLngBounds.Builder()
        for (geopoint in cluster.items) builder.include(
            geopoint.position
        )
        val bounds = builder.build()
        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onInfoWindowClick(marker: Marker) {
        if (marker.isInfoWindowShown) {
            marker.hideInfoWindow()
        } else {
            marker.showInfoWindow()
        }
    }

    override fun shouldRenderAsCluster(cluster: Cluster<Geopoint>?): Boolean {
        return cluster?.size!! > 2

    }

    private inner class MyCustomClusterItemInfoView internal constructor() : InfoWindowAdapter {
        private val clusterItemView: View =
            layoutInflater.inflate(R.layout.marker_info_window, null)

        override fun getInfoWindow(marker: Marker): View {
            val user =
                marker.tag as Geopoint? ?: return clusterItemView
            val itemNameTextView: TextView = clusterItemView.findViewById(R.id.itemNameTextView)
            itemNameTextView.text = marker.title
            return clusterItemView
        }

        override fun getInfoContents(marker: Marker): View? {
            return null
        }

    }

    init {
        clusterItemView = layoutInflater.inflate(R.layout.single_cluster_marker_view, null)
        clusterIconGenerator = IconGenerator(context)
        val drawable = ContextCompat.getDrawable(context, android.R.color.transparent)
        clusterIconGenerator.setBackground(drawable)
        clusterIconGenerator.setContentView(clusterItemView)
        clusterManager.setOnClusterClickListener(this)
        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager())
        googleMap.setOnInfoWindowClickListener(this)
        clusterManager.getMarkerCollection()
            .setOnInfoWindowAdapter(MyCustomClusterItemInfoView())
        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)
    }
}