package fahmy.smartplaces.features.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
import fahmy.smartplaces.R
import fahmy.smartplaces.base.BaseActivity
import fahmy.smartplaces.base.PlacesStates
import fahmy.smartplaces.base.helper.Utility
import fahmy.smartplaces.enitities.Result
import fahmy.smartplaces.viewmodel.PlacesViewModel
import kotlinx.android.synthetic.main.main_screen.*


//
// Created by Fahmy on 2/17/20.
//

class SmartPlaces : BaseActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesViewModel: PlacesViewModel
    private var mapFragment: SupportMapFragment? = null
    private var myMarker: Marker? = null
    private var googleMap: GoogleMap? = null
    private var myLocation: Result? = null
    var handler = Handler()
    private val adapter = AddressAdapter {
        val latlng = LatLng(it.geometry.location.lat, it.geometry.location.lng)
        myLocation = it
        myMarker?.position =
            latlng
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latlng,
                googleMap?.maxZoomLevel ?: 17F
            )
        )
    }

    private fun requestPermission(permission: String): Boolean {
        return if (CheckPermission(permission, this)) {
            true
        } else {
            RequestPermission(permission, this)
            false
        }
    }

    private fun CheckPermission(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun RequestPermission(permission: String, activity: Activity) {
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
                Utility.PermissionCode
            )


    }

    override fun onStop() {
        super.onStop()


    }



    override fun initialComponent() {
        if (!requestPermission(Manifest.permission.ACCESS_FINE_LOCATION) || !requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.e("Smart Places", "need location permission")
            finish()
        }
        adapter.results.clear()
        adapter.notifyDataSetChanged()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        placesViewModel = ViewModelProvider(this).get(PlacesViewModel::class.java)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    override fun clicks() {
        rv_address.adapter = adapter
        btn_location?.setOnClickListener {
            SmartPlacesInitialize.INSTANCE.callback(myLocation)
            finish()
        }
    }


    override fun getInflateView(): Int {
        return R.layout.main_screen
    }

    override fun initialObserve() {
        placesViewModel._states.observe(this, Observer {
            if (it != null) {
                handleResponse(it) { response ->
                    if (response is PlacesStates)
                        handleSuccessResponse(response)
                }
            }
        })
    }

    private fun handleSuccessResponse(result: PlacesStates) {
        when (result) {
            is PlacesStates.PlacesResponseSuccess -> {
                result.data?.results.takeIf { !it.isNullOrEmpty() }?.let { list ->
                    myLocation = list[0]
                    adapter.results = list.toMutableList()
                    adapter.notifyDataSetChanged()
                }
                println("Result is ${result.data}")
            }
        }
    }

    override fun removeObserve() {
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap?.isMyLocationEnabled = true
        googleMap?.setMaxZoomPreference(15F)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.apply {
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
                placesViewModel.getPlaces(SmartPlacesInitialize.INSTANCE.apiKey, this.latitude.toString(), this.longitude.toString())
            }


        }

        fusedLocationClient.lastLocation.addOnFailureListener {

            finish()
        }

        googleMap?.setOnMapLongClickListener { LatLng ->
            if (myMarker != null)
                myMarker?.position = LatLng
            LatLng?.apply {
                adapter.results.clear()
                adapter.notifyDataSetChanged()
                placesViewModel.getPlaces(SmartPlacesInitialize.INSTANCE.apiKey, this.latitude.toString(), this.longitude.toString())
            }
        }
        googleMap?.setOnMarkerClickListener {
            finish()
            true
        }
    }

}