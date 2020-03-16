package fahmy.smartplaces.base.helper

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import fahmy.smartplaces.R
import fahmy.smartplaces.base.MainRepository
import fahmy.smartplaces.enitities.GeneralResponse
import fahmy.smartplaces.features.home.SmartPlaces
import fahmy.smartplaces.repository.RepositoryClient
import okhttp3.Interceptor
import okhttp3.ResponseBody


/**
 * Created by fahmy on 16/April/2019
 */

fun Fragment.handleApiError(code: Int, e: ResponseBody?) {
    handelApiError(e) { message ->
        if (code == 401) {

        } else if (code == 524) {
        } else {
            if (message.isEmpty())
                Utility.errorDialog(requireContext(), getString(R.string.generic_error))
            else
                Utility.errorDialog(requireContext(), message)
        }
    }

}

fun Dialog.handleApiError(code: Int, e: ResponseBody?) {
    handelApiError(e) { message ->
        if (code == 401) {


        } else if (code == 524) {
        } else {
            if (message.isEmpty())
                Utility.errorDialog(context, context.getString(R.string.generic_error))
            else
                Utility.errorDialog(context, message)
        }
    }

}

fun Fragment.getAddress(
    showLoading: () -> Unit,
    hideLoading: () -> Unit,
    callBack: (Place) -> Unit

) {
    showLoading()
    val placesClient: PlacesClient = Places.createClient(requireContext())

    val placeFields = mutableListOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
    val request = FindCurrentPlaceRequest.newInstance(placeFields)
    if (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val placeResponse: Task<FindCurrentPlaceResponse?> =
            placesClient.findCurrentPlace(request)
        placeResponse.addOnCompleteListener { task: Task<FindCurrentPlaceResponse?> ->
            hideLoading()
            if (task.isSuccessful) {
                task.result.takeIf { it != null }?.let { currentPlace ->
                    currentPlace.takeIf { it.placeLikelihoods.size > 0 }?.let {
                        val place = it.placeLikelihoods[0].place
                        callBack(place)
                        Toast.makeText(requireContext(), place.address, Toast.LENGTH_LONG)
                            .show()

                    }

                }

            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    Log.e("Places", "Place not found: " + exception.message)
                }
            }
        }
    }
}


fun handelApiError(response: ResponseBody?, action: (String) -> Unit) {
    try {
        val dataSting = response?.string()
        val data = Gson().fromJson(dataSting, GeneralResponse::class.java)
        val message = data.msg
        action(message)
    } catch (e: Exception) {
        e.printStackTrace()
        action("")
    }
}



fun MainRepository.getApiClient(): RepositoryClient {
    return RepositoryClient.repositoryClient
}


fun Activity.handleApiError(code: Int, e: ResponseBody?) {
    handelApiError(e) { message ->
        when (code) {
            401 -> {


            }
            526 -> {
            }
            525 -> {
                //            verifiedScreen(this, Utility.AnyScreen, ManagerSharedPreferance.instance.getLoginDataUser()?.user?.mobile)
            }
            else -> {
                Utility.errorDialog(this, message)
            }
        }
    }

}


fun Activity.isInternetConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}


fun MainRepository.isInternetConnected(context: Context): Boolean {

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}

fun Fragment.isInternetConnected(): Boolean {
    return activity?.isInternetConnected() ?: false
}

fun Fragment.showInternetMessageError() {
    Utility.errorDialog(requireContext(), getString(R.string.noInternet))
}

fun Dialog.showInternetMessageError() {
    Utility.errorDialog(context, context.getString(R.string.noInternet))
}

fun Activity.showInternetMessageError() {
    Utility.errorDialog(this, getString(R.string.noInternet))
}

fun Fragment.requestPermission(permission: String): Boolean {
    return if (CheckPermission(permission, requireActivity())) {
        true
    } else {
        RequestPermission(permission, requireActivity())
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
        ActivityCompat.requestPermissions(activity, arrayOf(permission), Utility.PermissionCode)


}