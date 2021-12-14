package fahmy.smartplaces.features.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import fahmy.smartplaces.R
import fahmy.smartplaces.base.BaseActivity
import fahmy.smartplaces.base.PlacesStates
import fahmy.smartplaces.base.helper.Utility
import fahmy.smartplaces.databinding.MainScreenBinding
import fahmy.smartplaces.enitities.Result
import fahmy.smartplaces.viewmodel.PlacesViewModel
import kotlin.system.exitProcess


//
// Created by Fahmy on 2/17/20.
//

@Keep
class SmartPlaces : BaseActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesViewModel: PlacesViewModel
    private var mapFragment: SupportMapFragment? = null
    private var myMarker: Marker? = null
    private var googleMap: GoogleMap? = null
    private var myLocation: Result? = null
    private lateinit var bind: MainScreenBinding

    companion object {
        var callback: ((Result?) -> Unit)? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        fun start(context: Context, callback: (Result?) -> Unit) {
            this.callback = callback
            this.context = context
            context.startActivity(Intent(context, SmartPlaces::class.java))
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

        adapter.results.clear()
        adapter.notifyDataSetChanged()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        placesViewModel = ViewModelProvider(this).get(PlacesViewModel::class.java)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkAndRequestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mapFragment?.getMapAsync(this)


        } else {
            Log.e("Smart Places", "need location permission")
        }

    }


    override fun clicks() {
        bind.rvAddress.adapter = adapter
        bind.btnLocation.setOnClickListener {
            if (callback != null) {
                callback!!(myLocation)
            } else {
                Log.e("Smart Places ", "Call back is null")
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
        if (getString(R.string.google_maps_key).isEmpty() || getString(R.string.google_maps_key) == "Your Map Key" || getString(R.string.google_maps_key).length < 14) {
            Log.e("Smart Places", "Google Map Key should be add in your strings")
            exitProcess(0)
        }
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
                placesViewModel.getPlaces(getString(R.string.google_maps_key), this.latitude.toString(), this.longitude.toString())
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
                placesViewModel.getPlaces(getString(R.string.google_maps_key), this.latitude.toString(), this.longitude.toString())
            }
        }

    }

}