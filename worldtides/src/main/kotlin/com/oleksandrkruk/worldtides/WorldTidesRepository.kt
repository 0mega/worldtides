package com.oleksandrkruk.worldtides

import com.oleksandrkruk.worldtides.extremes.data.TideExtremesResponse
import com.oleksandrkruk.worldtides.extremes.models.Extreme
import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import com.oleksandrkruk.worldtides.extremes.models.TideType
import com.oleksandrkruk.worldtides.heights.data.TideHeightsResponse
import com.oleksandrkruk.worldtides.heights.models.Height
import com.oleksandrkruk.worldtides.heights.models.TideHeights
import com.oleksandrkruk.worldtides.models.TideDataType
import com.oleksandrkruk.worldtides.models.Tides
import com.oleksandrkruk.worldtides.models.TidesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

internal class WorldTidesRepository(
    private val tidesService: WorldTidesGateway,
    private val dateFormat: SimpleDateFormat
) {

    fun extremes(date: String, days: Int, lat: String, lon: String, apiKey: String, callback: (Result<TideExtremes>) -> Unit) {
        tidesService.extremes(date, days, lat, lon, apiKey).enqueue(object : Callback<TideExtremesResponse> {
            override fun onFailure(call: Call<TideExtremesResponse>, t: Throwable) {
                callback(Result.failure(t))
            }

            override fun onResponse(call: Call<TideExtremesResponse>, response: Response<TideExtremesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { tidesResponse ->
                        val extremes = tidesResponse.extremes.map { extreme ->
                            Extreme(dateFormat.parse(extreme.date), extreme.height, TideType.valueOf(extreme.type))
                        }
                        val tideExtremes = TideExtremes(extremes)
                        callback(Result.success(tideExtremes))
                    } ?: run {
                        callback(Result.failure(Error("Response is successful but failed getting body")))
                    }
                } else {
                    callback(Result.failure(Error("Response body is null or response is not successful")))
                }
            }
        })
    }

    fun heights(date: String, days: Int, lat: String, lon: String, apiKey: String, callback: (Result<TideHeights>) -> Unit) {
        tidesService.heights(date, days, lat, lon, apiKey).enqueue(object : Callback<TideHeightsResponse> {
            override fun onFailure(call: Call<TideHeightsResponse>, t: Throwable) {
                callback(Result.failure(t))
            }

            override fun onResponse(call: Call<TideHeightsResponse>, response: Response<TideHeightsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { heightsResponse ->
                        val heights = heightsResponse.heights.map { heightData ->
                            Height(dateFormat.parse(heightData.date)!!, heightData.height)
                        }
                        val tideHeights = TideHeights(heights)
                        callback(Result.success(tideHeights))
                    } ?: run {
                        callback(Result.failure(Error("Response is successful but failed getting body")))
                    }
                } else {
                    callback(Result.failure(Error("Response body is null or response is not successful")))
                }
            }
        })
    }

    fun tides(
        dataTypes: List<TideDataType>,
        date: String,
        days: Int,
        lat: String,
        lon: String,
        apiKey: String,
        callback: (Result<Tides>) -> Unit
    ) {
        val endpoint = "v2?" + dataTypes.joinToString("&") { it.queryValue }
        tidesService.tides(endpoint, date, days, lat, lon, apiKey).enqueue(object : Callback<TidesResponse> {
            override fun onFailure(call: Call<TidesResponse>, t: Throwable) {
                callback(Result.failure(t))
            }

            override fun onResponse(call: Call<TidesResponse>, response: Response<TidesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { tidesResponse ->
                        val tideHeights = tidesResponse.heights?.let { heightsList ->
                            val heights = heightsList.map { heightData ->
                                Height(dateFormat.parse(heightData.date)!!, heightData.height)
                            }
                            TideHeights(heights)
                        }
                        val tideExtremes = tidesResponse.extremes?.let { extremesList ->
                            val extremes = extremesList.map { extreme ->
                                Extreme(dateFormat.parse(extreme.date), extreme.height, TideType.valueOf(extreme.type))
                            }
                            TideExtremes(extremes)
                        }
                        val tides = Tides(heights = tideHeights, extremes = tideExtremes)
                        callback(Result.success(tides))
                    } ?: run {
                        callback(Result.failure(Error("Response is successful but failed getting body")))
                    }
                } else {
                    callback(Result.failure(Error("Response body is null or response is not successful")))
                }
            }
        })
    }
}

