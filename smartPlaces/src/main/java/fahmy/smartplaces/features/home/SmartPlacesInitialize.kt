package fahmy.smartplaces.features.home

import android.content.Context
import android.content.Intent
import fahmy.smartplaces.enitities.Result

class SmartPlacesInitialize {
    lateinit var apiKey: String
    var callback: ((Result?) -> Unit)? = null
    lateinit var context: Context

    fun startSmartPlaces(context: Context, callback: (Result?) -> Unit) {
        this.callback = callback
        this.context = context
        context.startActivity(Intent(context, SmartPlaces::class.java))
    }

    companion object {
        val INSTANCE: SmartPlacesInitialize = SmartPlacesInitialize()
    }
}