package com.bagasstb.cityguide

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        map.addMarker(
            markerOptions.icon(
                BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(resources, R.mipmap.ic_marker)
                )
            )
        )
        map.addMarker(
            MarkerOptions().position(LatLng(-6.173200, 106.786274))
                .title("Kosan")
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(resources, R.mipmap.ic_marker)
                    )
                )
        )
        val loc1 = Location("")
        loc1.latitude = location.latitude
        loc1.longitude = location.longitude

        val loc2 = Location("")
        loc2.latitude = -6.173200
        loc2.longitude = 106.786274

        val distanceInMeters = loc1.distanceTo(loc2)

        println(distanceInMeters)
        println("AYAAAAM")

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        map.isMyLocationEnabled = true

// 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.clear()
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        setUpMap()
        // Add a marker in Sydney and move the camera


    }
}
