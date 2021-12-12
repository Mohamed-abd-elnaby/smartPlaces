package fahmy.smartplaces.features.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.annotation.Keep
import fahmy.smartplaces.enitities.Result

@Keep
class SmartPlacesInitialize {
    companion object {
        var callback: ((Result?) -> Unit)? = null
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        fun startSmartPlaces(context: Context, callback: (Result?) -> Unit) {
            this.callback = callback
            this.context = context
            context.startActivity(Intent(context, SmartPlaces::class.java))
        }
    }


}