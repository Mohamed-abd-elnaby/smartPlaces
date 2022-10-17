package fahmy.smartplaces.repository

import fahmy.smartplaces.enitities.PlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by mohamed abd elnaby on 16/April/2019
 */

internal interface EndPoint {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun fetchPlaces(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("radius") radius: String
    ): Response<PlacesResponse>


}