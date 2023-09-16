package com.smartplaces.viewmodel

import androidx.lifecycle.LiveData
import com.smartplaces.base.CommonStates
import com.smartplaces.base.MainRepository
import com.smartplaces.base.PlacesStates
import com.smartplaces.base.SingleLiveEvent
import com.smartplaces.repository.RepositoryClient

//
// Created by Fahmy on 2/17/20.
//

internal class PlacesViewModel : MainRepository<PlacesStates>() {


    private var states = SingleLiveEvent<CommonStates<PlacesStates>>()
    var mStates: LiveData<CommonStates<PlacesStates>> = states
    fun getPlaces(apiKey: String, locationLat: String, locationLong: String) {
        fetchData(states, true) {
            val response = RepositoryClient.repositoryClient.fetchPlaces(apiKey, "$locationLat,$locationLong")
            if (response.isSuccessful)
                if (!response.body()?.results.isNullOrEmpty())
                    states.value =
                        CommonStates.Success(PlacesStates.PlacesResponseSuccess(response.body()))
                else
                    states.value = CommonStates.EmptyState
            else
                states.value = CommonStates.Error(response.code(), response.errorBody())

        }
    }
}