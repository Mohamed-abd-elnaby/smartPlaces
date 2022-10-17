package fahmy.smartplaces.enitities

import androidx.annotation.Keep

@Keep
 data class Geometry(
    val location: Location,
    val viewport: Viewport?=null
)