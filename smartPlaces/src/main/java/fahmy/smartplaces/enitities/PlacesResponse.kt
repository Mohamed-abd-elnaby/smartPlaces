package fahmy.smartplaces.enitities

import androidx.annotation.Keep

@Keep
internal data class PlacesResponse(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)