package com.example.tsymbaliuk_functional

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.clustering.ClusterManager


class MapFragment :
    Fragment() {

    private lateinit var mainVM: MainViewModel
    private lateinit var mapFragment: SupportMapFragment
    lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_map, container, false)
        mapFragment =
            childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment

        mainVM = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        mapFragment.getMapAsync { mMap ->
            map = mMap
            map.mapType = GoogleMap.MAP_TYPE_NORMAL

            map.setOnInfoWindowClickListener { marker ->
                if (marker.isInfoWindowShown) {
                    marker.hideInfoWindow()
                } else {
                    marker.showInfoWindow()
                }
            }

            map.setOnMapLoadedCallback {

                map.setOnMapLongClickListener {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder.setTitle("Введите название")

                    val input = EditText(context)
                    builder.setView(input)

                    builder.setPositiveButton("OK"
                    ) { dialog, which ->
                        val nameET = input.text.toString()

                        val data: HashMap<String, Any> = hashMapOf(
                            "name" to nameET,
                            "geopoint" to GeoPoint(it.latitude, it.longitude)
                        )

                        MyApplication.instance.database.collection("city_coordinates_ua")
                            .add(data)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                            }
                        map.addMarker(MarkerOptions().position(it))

                    }
                    builder.setNegativeButton("Cancel"
                    ) { dialog, which -> dialog.cancel() }
                    builder.show()

                }

                mainVM.listOfGeopoints.observe(this, Observer { geopointsList ->
                    if (arguments != null) {
                        setUpClusterManager(
                            map, geopointsList[MapFragmentArgs.fromBundle(
                                arguments!!
                            ).positionargs], geopointsList
                        )
                    } else {
                        setUpClusterManager(
                            map, mainVM.currentGeopoint.value!!, geopointsList
                        )
                    }
                })

                mainVM.currentGeopoint.observe(this, Observer { currentGeopoint ->
                    if (currentGeopoint.latLng.latitude != 0.0 && currentGeopoint.latLng.longitude != 0.0
                    ) {
                        zoomMap(currentGeopoint)
                    }
                })
            }

        }

        return rootView
    }

    private fun setUpClusterManager(googleMap: GoogleMap, geopoint: Geopoint, geopointsList: ArrayList<Geopoint>) {
        val clusterManager =
            ClusterManager<Geopoint>(context!!, googleMap)
        val markerClusterRenderer =
            MarkerClusterRenderer(context!!, googleMap, clusterManager, geopoint)
        clusterManager.renderer = markerClusterRenderer
        clusterManager.addItems(geopointsList)
        clusterManager.cluster()
    }

    private fun zoomMap(currentGeopoint: Geopoint) {
        val destinationGeopoint: Geopoint = if (arguments != null) {
            mainVM.listOfGeopoints.value!![MapFragmentArgs.fromBundle(arguments!!).positionargs]
        } else {
            currentGeopoint
        }

        val googlePlex = CameraPosition.builder()
            .target(
                LatLng(
                    destinationGeopoint.latLng.latitude,
                    destinationGeopoint.latLng.longitude
                )
            )
            .zoom(10f)
            .bearing(0f)
            .tilt(45f)
            .build()
        map.animateCamera(
            CameraUpdateFactory.newCameraPosition(googlePlex),
            10000,
            null
        )
    }

}


