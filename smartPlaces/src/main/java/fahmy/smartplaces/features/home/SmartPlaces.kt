package fahmy.smartplaces.features.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
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
import fahmy.smartplaces.base.BaseDialogFragment
import fahmy.smartplaces.base.PlacesStates
import fahmy.smartplaces.base.helper.Utility
import fahmy.smartplaces.enitities.Result
import fahmy.smartplaces.viewmodel.PlacesViewModel
import kotlinx.android.synthetic.main.main_screen.*


//
// Created by Fahmy on 2/17/20.
//

class SmartPlaces() : BaseDialogFragment(), OnMapReadyCallback {
    lateinit var callback: (Result?) -> Unit
    lateinit var onFinish: () -> Unit
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesViewModel: PlacesViewModel
    private var mapFragment: SupportMapFragment? = null
    private var myMarker: Marker? = null
    private var googleMap: GoogleMap? = null
    private var myLocation: Result? = null
    private var googleApiKey = ""
    var handler = Handler()

    private fun requestPermission(permission: String, context: Activity): Boolean {
        return if (CheckPermission(permission, context)) {
            true
        } else {
            RequestPermission(permission, context)
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

    companion object {
        lateinit var mContext: Context
        private var instance: SmartPlaces? = null

        fun initialize(googleApiKey: String, context: Context) {

            if (googleApiKey.isNotEmpty()) {
                mContext = context
                instance = SmartPlaces()
                instance?.googleApiKey = googleApiKey
            } else {
                MSG("Api key is empty.")
            }
        }

        fun start(
            context: Activity, fragmentManager: FragmentManager,
            callback: (Result?) -> Unit,onFinsh:()->Unit
        ) {
            if (instance == null) {
                MSG("Please initialize Smart places !!")
            }
            instance.takeIf { it != null }?.apply {

                this.callback = callback
                this.onFinish=onFinsh
                if (requestPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        context
                    ) || requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, context)
                ) {
                    instance?.showNow(fragmentManager, "")

                }
            }


        }

        private fun MSG(msg: String) {
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()

        }

        private fun MSG(MSG: String, time: Int) {
            Toast.makeText(mContext, MSG, time).show()
        }
    }



    override fun onStop() {
        super.onStop()


    }

    override fun onDestroy() {
        super.onDestroy()
        onFinish()
    }


    override fun initialComponent() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        placesViewModel = ViewModelProvider(this).get(PlacesViewModel::class.java)

        mapFragment =
            fragmentManager?.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
//        view?.findViewById<RecyclerView>(Rv).layoutManager = LinearLayoutManager(context)
    }


    override fun clicks() {
        btn_location?.setOnClickListener {
            callback(myLocation)
            dismiss()
        }
    }


    override fun onDetach() {
        super.onDetach()
        onFinish()
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
                    val adapter = AddressAdapter(list) {
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
                    rv_address.adapter = adapter
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
                placesViewModel.getPlaces(googleApiKey, this.latitude.toString(), this.longitude.toString())
            }


        }
        fusedLocationClient.lastLocation.addOnFailureListener {
            dismiss()
        }
        googleMap?.setOnMapLongClickListener { LatLng ->
            if (myMarker != null)
                myMarker?.position = LatLng
            LatLng?.apply {
                placesViewModel.getPlaces(googleApiKey, this.latitude.toString(), this.longitude.toString())
            }
        }
        googleMap?.setOnMarkerClickListener {
            dismiss()
            true
        }
    }

}