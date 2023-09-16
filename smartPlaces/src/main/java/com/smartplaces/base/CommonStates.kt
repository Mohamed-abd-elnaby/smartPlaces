package com.smartplaces.base

import com.smartplaces.enitities.PlacesResponse
import okhttp3.ResponseBody

internal sealed class CommonStates<out T> {
    internal data object LoadingShow : CommonStates<Nothing>()
    internal data object NoInternet : CommonStates<Nothing>()
    internal data object EmptyState : CommonStates<Nothing>()
    internal data class Success<out R>(val data: R) : CommonStates<R>()
    internal data class Error(val code: Int, val exp: ResponseBody?) : CommonStates<Nothing>()
}

internal sealed class PlacesStates {
    internal data class PlacesResponseSuccess(val data: PlacesResponse?) : PlacesStates()


}
