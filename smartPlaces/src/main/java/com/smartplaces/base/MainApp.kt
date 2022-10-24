package com.smartplaces.base

import android.app.Application


/*
Created by mohamed fahmy on 14/12/2021.
*/
internal class MainApp : Application() {
    companion object{
        lateinit var instance:MainApp
    }
    override fun onCreate() {
        super.onCreate()
        instance=this
    }
}