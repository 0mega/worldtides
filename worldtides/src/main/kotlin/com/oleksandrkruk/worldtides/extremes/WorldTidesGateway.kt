package com.oleksandrkruk.worldtides.extremes

import com.oleksandrkruk.worldtides.extremes.data.TideExtremesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface WorldTidesGateway {
    @GET("v2?extremes")
    fun extremes(
        @Query("date") date: String,
        @Query("days") days: Int,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("key") apiKey: String
    ) : Call<TideExtremesResponse>
}
