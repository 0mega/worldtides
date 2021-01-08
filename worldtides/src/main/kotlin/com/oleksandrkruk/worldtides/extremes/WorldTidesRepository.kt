package com.oleksandrkruk.worldtides.extremes

import com.oleksandrkruk.worldtides.extremes.data.TideExtremesResponse
import com.oleksandrkruk.worldtides.extremes.models.Extreme
import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import com.oleksandrkruk.worldtides.extremes.models.TideType
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
}
