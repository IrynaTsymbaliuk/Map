package com.example.tsymbaliuk_functional

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


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



            mainVM.listOfGeopoints.observe(this, Observer { geopointsList ->
                //if (::map.isInitialized) {
                    drawMap(geopointsList)
                //}
            })

            mainVM.currentGeopoint.observe(this, Observer { currentGeopoint ->
                if (currentGeopoint.longitude != 0.0 && currentGeopoint.latitude != 0.0
                    //&& ::map.isInitialized
                    ) {
                    zoomMap()
                }
            })
            //map.clear()
        }

        return rootView
    }

    private fun drawMap(geopointsList: ArrayList<Geopoint>?) {
        if (geopointsList != null && geopointsList.size == 0) {
            geopointsList.forEach { geopoint ->
                if (geopoint.latitude != null && geopoint.longitude != null) {
                    map.addMarker(
                        MarkerOptions()
                            .position(
                                LatLng(
                                    geopoint.latitude!!,
                                    geopoint.longitude!!
                                )
                            )
                            .title(geopoint.name)
                            .icon(
                                bitmapDescriptorFromVector(
                                    activity!!,
                                    R.drawable.ic_place_black_24dp
                                )
                            )
                    )
                }
            }
        }
    }

    private fun zoomMap() {
        val googlePlex = CameraPosition.builder()
            .target(
                LatLng(
                    mainVM.currentGeopoint.value!!.latitude!!,
                    mainVM.currentGeopoint.value!!.longitude!!
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

    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}
