package fahmy.smartplaces.enitities

import androidx.annotation.Keep

@Keep
data class Result(
    val geometry: Geometry,
    val icon: String?=null,
    val id: String?=null,
    val name: String,
    val opening_hours: OpeningHours?=null,
    val photos: List<Photo>?=null,
    val place_id: String?=null,
    val plus_code: PlusCode?=null,
    val price_level: Int?=null,
    val rating: Double?=null,
    val reference: String?=null,
    val scope: String?=null,
    val types: List<String>?=null,
    val user_ratings_total: Int?=null,
    val vicinity: String?=null
)