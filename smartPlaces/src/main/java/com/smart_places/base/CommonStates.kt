package com.smartplaces.base

import com.smartplaces.enitities.PlacesResponse
import okhttp3.ResponseBody

internal sealed class CommonStates<out T> {
    internal object LoadingShow : CommonStates<Nothing>()
    internal  object NoInternet : CommonStates<Nothing>()
    internal object EmptyState : CommonStates<Nothing>()
    internal data class Success<out R>(val data: R) : CommonStates<R>()
    internal data class Error(val code: Int, val exp: ResponseBody?) : CommonStates<Nothing>()
}

internal sealed class FCMState {

    internal data class FCMSuccess(val id: Int, val title: String, val body: String) : FCMState()
}

internal sealed class PlacesStates {
    internal data class PlacesResponseSuccess(val data: PlacesResponse?) : PlacesStates()


}
