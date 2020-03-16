package fahmy.smartplaces.base

import fahmy.smartplaces.enitities.PlacesResponse
import okhttp3.ResponseBody

sealed class CommonStates<out T> {
    object LoadingShow : CommonStates<Nothing>()
    object NoInternet : CommonStates<Nothing>()
    object EmptyState : CommonStates<Nothing>()
    data class Success<out R>(val data: R) : CommonStates<R>()
    data class Error(val code: Int, val exp: ResponseBody?) : CommonStates<Nothing>()
}

sealed class FCMState {

    data class FCMSuccess(val id: Int, val title: String, val body: String) : FCMState()
}

sealed class PlacesStates {
    data class PlacesResponseSuccess(val data: PlacesResponse?) : PlacesStates()


}
