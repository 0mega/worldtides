package com.oleksandrkruk.worldtides

import com.oleksandrkruk.worldtides.extremes.data.TideExtremesResponse
import com.oleksandrkruk.worldtides.extremes.data.toExtreme
import com.oleksandrkruk.worldtides.extremes.data.toTideExtremes
import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import com.oleksandrkruk.worldtides.heights.data.TideHeightsResponse
import com.oleksandrkruk.worldtides.heights.data.toHeight
import com.oleksandrkruk.worldtides.heights.data.toTideHeights
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
        tidesService.extremes(date, days, lat, lon, apiKey).enqueueMapped(
            transform = { it.toTideExtremes(dateFormat) },
            callback = callback
        )
    }

    fun heights(date: String, days: Int, lat: String, lon: String, apiKey: String, callback: (Result<TideHeights>) -> Unit) {
        tidesService.heights(date, days, lat, lon, apiKey).enqueueMapped(
            transform = { it.toTideHeights(dateFormat) },
            callback = callback
        )
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
        tidesService.tides(endpoint, date, days, lat, lon, apiKey).enqueueMapped(
            transform = { tidesResponse ->
                Tides(
                    heights = tidesResponse.heights?.let { heightsList ->
                        TideHeights(heightsList.map { it.toHeight(dateFormat) })
                    },
                    extremes = tidesResponse.extremes?.let { extremesList ->
                        TideExtremes(extremesList.map { it.toExtreme(dateFormat) })
                    }
                )
            },
            callback = callback
        )
    }
}

/**
 * Extension function to enqueue a Retrofit call with automatic mapping.
 * Centralizes callback handling to reduce boilerplate across repository methods.
 *
 * @param transform Function to transform the response body into the desired result type.
 * @param callback Callback to receive the Result (success or failure).
 */
private inline fun <T, R> Call<T>.enqueueMapped(
    crossinline transform: (T) -> R,
    crossinline callback: (Result<R>) -> Unit
) {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            callback(Result.failure(t))
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let { body ->
                    callback(Result.success(transform(body)))
                } ?: callback(Result.failure(Error("Response is successful but failed getting body")))
            } else {
                callback(Result.failure(Error("Response body is null or response is not successful")))
            }
        }
    })
}

