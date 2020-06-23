package fahmy.smartplaces.features.home

import android.content.Context
import android.content.Intent
import androidx.annotation.Keep
import fahmy.smartplaces.enitities.Result

@Keep
class SmartPlacesInitialize {
    var apiKey: String = ""
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