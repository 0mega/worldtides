package com.oleksandrkruk.worldtides

import com.oleksandrkruk.worldtides.extremes.data.TideExtremesResponse
import com.oleksandrkruk.worldtides.heights.data.TideHeightsResponse
import com.oleksandrkruk.worldtides.models.TidesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

internal interface WorldTidesGateway {
    @GET("v2?extremes")
    fun extremes(
        @Query("date") date: String,
        @Query("days") days: Int,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("key") apiKey: String
    ) : Call<TideExtremesResponse>

    @GET("v2?heights")
    fun heights(
        @Query("date") date: String,
        @Query("days") days: Int,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("key") apiKey: String
    ) : Call<TideHeightsResponse>

    /**
     * Dynamic endpoint for flexible tide data requests.
     * The endpoint path should include the data type query params (e.g., "v2?heights&extremes").
     */
    @GET
    fun tides(
        @Url endpoint: String,
        @Query("date") date: String,
        @Query("days") days: Int,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("key") apiKey: String
    ) : Call<TidesResponse>
}

