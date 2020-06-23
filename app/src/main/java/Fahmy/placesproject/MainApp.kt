package Fahmy.placesproject

import android.app.Application
import fahmy.smartplaces.features.home.SmartPlacesInitialize

//
// Created by fahmy on 3/16/20.
//

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
//        SmartPlaces.initialize(getString(R.string.apiKey), this)
        SmartPlacesInitialize.INSTANCE.apiKey = getString(R.string.apiKey)
        SmartPlacesInitialize.INSTANCE.context = this.applicationContext
    }
}