package com.smartplaces.base.helper

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.smartplaces.R
import com.smartplaces.enitities.GeneralResponse
import com.smartplaces.features.home.SmartPlaces
import okhttp3.ResponseBody


/**
 * Created by fahmy on 16/April/2019
 */


 internal fun Fragment.handleApiError(e: ResponseBody?) {
    handelApiError(e) { message ->
        if (message.isEmpty())
            Utility.errorDialog(requireContext(), getString(R.string.generic_error))
        else
            Utility.errorDialog(requireContext(), message)

    }

}

 internal fun Dialog.handleApiError(e: ResponseBody?) {
    handelApiError(e) { message ->
        if (message.isEmpty())
            Utility.errorDialog(context, context.getString(R.string.generic_error))
        else
            Utility.errorDialog(context, message)

    }

}



internal fun handelApiError(response: ResponseBody?, action: (String) -> Unit) {
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


internal fun Activity.handleApiError(code: Int, e: ResponseBody?) {
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

internal fun isInternetAvailable(context: Context?): Boolean {
    var result = false
    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}

inline internal fun Fragment.isInternetConnected(ifConnected: () -> Unit, ifNotConnected: () -> Unit) {

    if (isInternetAvailable(requireContext()))
        ifConnected()
    else
        ifNotConnected()
}

inline internal fun Activity.isInternetConnected(ifConnected: () -> Unit, ifNotConnected: () -> Unit) {
    if (isInternetAvailable(this))
        ifConnected()
    else
        ifNotConnected()
}

internal fun isInternetConnected(): Boolean {
    return isInternetAvailable(SmartPlaces.activity)
}

internal fun Fragment.showInternetMessageError() {
    Utility.errorDialog(requireContext(), getString(R.string.noInternet))
}

internal fun Dialog.showInternetMessageError() {
    Utility.errorDialog(context, context.getString(R.string.noInternet))
}

internal fun Activity.showInternetMessageError() {
    Utility.errorDialog(this, getString(R.string.noInternet))
}

internal fun Fragment.requestPermission(permission: String): Boolean {
    return if (CheckPermission(permission, requireActivity())) {
        true
    } else {
        RequestPermission(permission, requireActivity())
        false
    }
}


private  fun CheckPermission(permission: String, activity: Activity): Boolean {
    return ContextCompat.checkSelfPermission(
        activity,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

private  fun RequestPermission(permission: String, activity: Activity) {
    if (permission == Manifest.permission.ACCESS_COARSE_LOCATION || permission == Manifest.permission.ACCESS_FINE_LOCATION) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            Utility.PermissionCodeLocation
        )
    } else
        ActivityCompat.requestPermissions(activity, arrayOf(permission), Utility.PermissionCode)


}