package com.smartplaces.features.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Keep
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.smartplaces.R
import com.smartplaces.base.BaseActivity
import com.smartplaces.base.PlacesStates
import com.smartplaces.base.helper.Utility
import com.smartplaces.databinding.MainScreenBinding
import com.smartplaces.enitities.Geometry
import com.smartplaces.enitities.Location
import com.smartplaces.enitities.Result
import com.smartplaces.viewmodel.PlacesViewModel
import java.util.*


//
// Created by Fahmy on 2/17/20.
//

@Keep
class SmartPlaces : BaseActivity(), OnMapReadyCallback {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var placesViewModel: PlacesViewModel
    private var mapFragment: SupportMapFragment? = null
    private var myMarker: Marker? = null
    private var googleMap: GoogleMap? = null
    private var myLocation: Result? = null
    private lateinit var bind: MainScreenBinding

    companion object {
        var callback: ((Result?) -> Unit)? = null
        var findNearbyPlaces: Boolean = false

        @SuppressLint("StaticFieldLeak")
        lateinit var activity: Activity
        fun start(activity: Activity, findNearbyPlaces: Boolean = false, callback: (Result?) -> Unit) {
            this.callback = callback
            this.activity = activity
            this.findNearbyPlaces = findNearbyPlaces
            activity.startActivity(Intent(activity, SmartPlaces::class.java))
        }
    }

    private val adapter = AddressAdapter {
        val latLng = LatLng(it.geometry.location.lat, it.geometry.location.lng)
        myLocation = it
        myMarker?.position =
            latLng
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                googleMap?.maxZoomLevel ?: 17F
            )
        )
    }

    private fun checkAndRequestPermission(permission: String): Boolean {
        return if (checkPermission(permission, this)) {
            true
        } else {
            requestPermission(permission, this)
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun checkPermission(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(permission: String, activity: Activity) {
        if (permission == Manifest.permission.ACCESS_COARSE_LOCATION || permission == Manifest.permission.ACCESS_FINE_LOCATION) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                Utility.PermissionCodeLocation
            )
        } else
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                Utility.PermissionCodeLocation
            )


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Utility.PermissionCodeLocation && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            if (checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkAndRequestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                mapFragment?.getMapAsync(this)


            } else {
                Log.e("Smart Places", "need location permission")
            }
        else {
            Toast.makeText(this, "Permission Must be provided", Toast.LENGTH_LONG).show()
            finish()
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun initialComponent() {
        if (findNearbyPlaces) {
            adapter.results.clear()
            adapter.notifyDataSetChanged()
            bind.rvAddress.adapter = adapter
            bind.rvAddress.visibility = View.VISIBLE
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        placesViewModel = ViewModelProvider(this)[PlacesViewModel::class.java]
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkAndRequestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mapFragment?.getMapAsync(this)
        } else {
            Log.e("Smart Places", "need location permission")
        }

    }


    override fun clicks() {
        bind.btnLocation.setOnClickListener {
            if (myLocation != null) {
                if (myLocation?.name.isNullOrEmpty())
                    getAddress(myLocation?.geometry?.location?.lat ?: 0.0, myLocation?.geometry?.location?.lng ?: 0.0) {
                        myLocation?.name = it
                        callback?.let { it(myLocation) }
                    }
                else {
                    callback?.let { it(myLocation) }
                }
            } else {
                Log.e("Smart Places ", "Location Not Chosen")
            }
            finish()
        }
    }


    override fun getInflateView(): View {
        bind = MainScreenBinding.inflate(layoutInflater)
        return bind.root
    }

    override fun getState(): LiveData<*> {
        return placesViewModel._states
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun invoke(p1: Any) {
        when (p1) {
            is PlacesStates.PlacesResponseSuccess -> {
                p1.data?.results.takeIf { !it.isNullOrEmpty() }?.let { list ->
                    myLocation = list[0]
                    adapter.results = list.toMutableList()
                    adapter.notifyDataSetChanged()
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap?.isMyLocationEnabled = true
        googleMap?.setMaxZoomPreference(15F)
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            location?.apply {
                myLocation = Result(Geometry(Location(latitude, longitude)))
                getAddress(latitude, longitude) { name ->
                    myLocation?.name = name
                }
                googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            this.latitude,
                            this.longitude
                        ), 15F
                    )
                )
                myMarker = googleMap?.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            this.latitude,
                            this.longitude
                        )
                    )
                )
            }


        }
        fusedLocationClient?.lastLocation?.addOnFailureListener {
            finish()
        }
        googleMap?.setOnMapLongClickListener { LatLng ->
            showLoading()
            if (myMarker != null)
                myMarker?.position = LatLng

            if (findNearbyPlaces)
                LatLng?.apply {
                    adapter.results.clear()
                    adapter.notifyDataSetChanged()
                    placesViewModel.getPlaces(getString(R.string.google_maps_key), this.latitude.toString(), this.longitude.toString())
                }
            else {
                myLocation = Result(Geometry(Location(LatLng.latitude, LatLng.longitude)))
                getAddress(LatLng.latitude, LatLng.longitude) {
                    myLocation?.name = it
                }
            }
            hideLoading()
        }

    }

    private fun getAddress(lat: Double, lng: Double, callback: (String?) -> Unit) {
        val geocoder = Geocoder(this, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lng, 1) { p0 ->
                p0.takeIf { it.isNotEmpty() }?.let {
                    callback(displayAddress(it[0]))
                }
            }
        } else {
            val addresses: MutableList<Address?>? = geocoder.getFromLocation(lat, lng, 1)
            addresses.takeIf { !it.isNullOrEmpty() }?.let {
                callback(displayAddress(it[0]))
            }
        }


    }

    private fun displayAddress(obj: Address?): String {
        return try {
            var add: String? = obj?.getAddressLine(0)
            add = """ $add${obj?.countryName}""".trimIndent()
            add = """$add${obj?.countryCode}""".trimIndent()
            add = """ $add${obj?.adminArea} """.trimIndent()
            add = """$add${obj?.postalCode}""".trimIndent()
            add = """$add${obj?.subAdminArea}""".trimIndent()
            add = """$add ${obj?.locality} """.trimIndent()
            add = """$add${obj?.subThoroughfare} """.trimIndent()
            add.trimIndent()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

}