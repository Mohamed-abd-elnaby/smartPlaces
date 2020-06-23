package fahmy.smartplaces.enitities

import androidx.annotation.Keep

@Keep
data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
)