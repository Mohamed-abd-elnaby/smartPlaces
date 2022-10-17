@file:Suppress("DEPRECATION")

package fahmy.smartplaces.base.helper

import android.app.AlertDialog
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import fahmy.smartplaces.R
import java.util.*


internal object Utility {

    const val PermissionCodeLocation = 600
    const val PermissionCode = 601


    val local: Locale
        get() = Locale("en")
    lateinit var alertDialog: AlertDialog


    fun errorDialog(context: Context, msg: String) {
        val params =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        params.setMargins(10, 10, 10, 10)
        try {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            val tv = TextView(context)
            tv.text = context.getString(R.string.error)
            tv.textSize = 16F
            tv.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            tv.setTextColor(context.resources.getColor(R.color.red))
            builder.setCustomTitle(tv)
            builder.setMessage(msg)
            builder.setNegativeButton(context.getString(R.string.ok)) { _, _ ->
            }
            alertDialog = builder.create()
            if (alertDialog.isShowing) {
                alertDialog.dismiss()
            } else {
                alertDialog.show()

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }



}
