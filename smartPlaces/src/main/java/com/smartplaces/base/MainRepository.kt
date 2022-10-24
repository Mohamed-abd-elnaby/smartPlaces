package com.smartplaces.base


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartplaces.base.helper.isInternetConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by mohamed abd elnaby on 16/April/2019
 */


internal abstract class MainRepository : ViewModel() {


    fun fetchData(states: Any, showProgress: Boolean, worker: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            if (isInternetConnected()) {
                if (showProgress)
                    (states as MutableLiveData<*>).value = CommonStates.LoadingShow
                worker()

            } else {
                (states as MutableLiveData<*>).value = CommonStates.NoInternet
            }
        }

    fun fetchData(worker: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            if (isInternetConnected()) {
                worker()
            }
        }


}