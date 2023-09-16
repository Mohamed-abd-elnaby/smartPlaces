package com.smartplaces.base


import androidx.lifecycle.ViewModel
import com.smartplaces.base.helper.isInternetConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by mohamed abd elnaby on 16/April/2019
 */


internal abstract  class  MainRepository <T> : ViewModel() {


    fun fetchData(states: SingleLiveEvent<CommonStates<T>>, showProgress: Boolean, worker: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            if (isInternetConnected()) {
                if (showProgress)
                    states.value = CommonStates.LoadingShow
                worker()

            } else {
                states.value = CommonStates.NoInternet
            }
        }

    fun fetchData(worker: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            if (isInternetConnected()) {
                worker()
            }
        }


}
