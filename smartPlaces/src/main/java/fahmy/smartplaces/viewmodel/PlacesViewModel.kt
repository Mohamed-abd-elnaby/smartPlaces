package fahmy.smartplaces.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fahmy.smartplaces.base.CommonStates
import fahmy.smartplaces.base.MainRepository
import fahmy.smartplaces.base.PlacesStates
import fahmy.smartplaces.repository.RepositoryClient

//
// Created by Fahmy on 2/17/20.
//

class PlacesViewModel() : MainRepository() {


    private var states = MutableLiveData<CommonStates<PlacesStates>>()
    var _states: LiveData<CommonStates<PlacesStates>> = states
    fun getPlaces(apiKey: String, loc_lat: String, loc_long: String) {
        fetchData(states, true) {
            val response = RepositoryClient.repositoryClient.fetchPlaces(apiKey, "$loc_lat,$loc_long")
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