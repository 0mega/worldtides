package com.oleksandrkruk.worldtides

import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import com.oleksandrkruk.worldtides.heights.models.TideHeights
import com.oleksandrkruk.worldtides.models.TideDataType
import com.oleksandrkruk.worldtides.models.Tides
import java.text.SimpleDateFormat
import java.util.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class WorldTides
private constructor(
        private val apiKey: String,
        private val tidesRepository: WorldTidesRepository,
        private val inputDateFormat: SimpleDateFormat
) {

    class Builder {
        private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.US)
        private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private var okHttpClient: OkHttpClient? = null
        private var loggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE

        private val tidesGateway by lazy {
            RetrofitClient("https://www.worldtides.info/api/", okHttpClient, loggingLevel)
                    .tidesService
        }
        private val tidesRepository: WorldTidesRepository by lazy {
            WorldTidesRepository(tidesGateway, apiDateFormat)
        }

        fun client(client: OkHttpClient): Builder {
            this.okHttpClient = client
            return this
        }

        fun loggingLevel(level: HttpLoggingInterceptor.Level): Builder {
            this.loggingLevel = level
            return this
        }

        /**
         * Create an instance of [WorldTides] using a given [apiKey]. The [apiKey] will be used for
         * all the calls to the worldtides.info API.
         *
         * Note: You should avoid creating many instances of [WorldTides] as it is a costly
         * operation. Your app should cache an instance of [WorldTides] and reuse it for all the
         * requests you make during a user session.
         */
        fun build(apiKey: String): WorldTides {
            return WorldTides(apiKey, tidesRepository, inputDateFormat)
        }
    }

    /**
     * Returns only the extremes of the tides (Highs and Lows) between [date] and the number of
     * [days] in future.
     *
     * @param [date] should be the starting date from which the tide extremes prediction are
     * requested.
     * @param [days] should be the number of days for which tide extremes are requested. This number
     * will add to the [date] and will form a time range. E.g.: If the given [date] is 2020-12-01
     * and [days] is 3 it means that the user will receive all tides between 2020-12-01 and
     * 2020-12-03.
     * @param [lat] is the latitude of the location for which the tides are requested.
     * @param [lon] is the longitude of the location for which the tides are requested.
     * @param [callback] is the callback that will return the results. The [callback] will offer
     * tide extremes wrapped in a [Result]. When the operation is successful, the result will
     * contain the tide extremes otherwise it will provide an error.
     */
    fun getTideExtremes(
            date: Date,
            days: Int,
            lat: String,
            lon: String,
            callback: (Result<TideExtremes>) -> Unit
    ) {
        val dateStr = inputDateFormat.format(date)
        tidesRepository.extremes(dateStr, days, lat, lon, apiKey, callback)
    }

    /**
     * Returns only the extremes of the tides (Highs and Lows) between [date] and the number of
     * [days] in future. **This method exists for Java interoperability only.**
     *
     * @param [date] should be the starting date from which the tide extremes prediction are
     * requested.
     * @param [days] should be the number of days for which tide extremes are requested. This number
     * will add to the [date] and will form a time range. E.g.: If the given [date] is 2020-12-01
     * and [days] is 3 it means that the user will receive all tides between 2020-12-01 and
     * 2020-12-03.
     * @param [lat] is the latitude of the location for which the tides are requested.
     * @param [lon] is the longitude of the location for which the tides are requested.
     * @param [callback] is the callback that will return either tide or an error.
     */
    fun getTideExtremes(
            date: Date,
            days: Int,
            lat: String,
            lon: String,
            callback: TidesCallback<TideExtremes>
    ) {
        val dateStr = inputDateFormat.format(date)
        tidesRepository.extremes(dateStr, days, lat, lon, apiKey) { result ->
            result.onFailure { callback.error(Error(it)) }
            result.onSuccess { callback.result(it) }
        }
    }

    /**
     * Returns the predicted tide heights between [date] and the number of [days] in future.
     *
     * @param [date] should be the starting date from which the tide heights prediction are
     * requested.
     * @param [days] should be the number of days for which tide heights are requested.
     * @param [lat] is the latitude of the location for which the tides are requested.
     * @param [lon] is the longitude of the location for which the tides are requested.
     * @param [callback] is the callback that will return the results wrapped in a [Result].
     */
    fun getTideHeights(
            date: Date,
            days: Int,
            lat: String,
            lon: String,
            callback: (Result<TideHeights>) -> Unit
    ) {
        val dateStr = inputDateFormat.format(date)
        tidesRepository.heights(dateStr, days, lat, lon, apiKey, callback)
    }

    /**
     * Returns the predicted tide heights between [date] and the number of [days] in future. **This
     * method exists for Java interoperability only.**
     *
     * @param [date] should be the starting date from which the tide heights prediction are
     * requested.
     * @param [days] should be the number of days for which tide heights are requested.
     * @param [lat] is the latitude of the location for which the tides are requested.
     * @param [lon] is the longitude of the location for which the tides are requested.
     * @param [callback] is the callback that will return either tide heights or an error.
     */
    fun getTideHeights(
            date: Date,
            days: Int,
            lat: String,
            lon: String,
            callback: TidesCallback<TideHeights>
    ) {
        val dateStr = inputDateFormat.format(date)
        tidesRepository.heights(dateStr, days, lat, lon, apiKey) { result ->
            result.onFailure { callback.error(Error(it)) }
            result.onSuccess { callback.result(it) }
        }
    }

    /**
     * Returns flexible tide data based on the specified [dataTypes]. Supports stacking multiple
     * data types in a single API call.
     *
     * @param [date] should be the starting date from which the tide data is requested.
     * @param [days] should be the number of days for which tide data is requested.
     * @param [lat] is the latitude of the location for which the tides are requested.
     * @param [lon] is the longitude of the location for which the tides are requested.
     * @param [dataTypes] list of data types to request (e.g., HEIGHTS, EXTREMES).
     * @param [callback] is the callback that will return the results wrapped in a [Result].
     */
    fun getTides(
            date: Date,
            days: Int,
            lat: String,
            lon: String,
            dataTypes: List<TideDataType>,
            callback: (Result<Tides>) -> Unit
    ) {
        val dateStr = inputDateFormat.format(date)
        tidesRepository.tides(dataTypes, dateStr, days, lat, lon, apiKey, callback)
    }

    /**
     * Returns flexible tide data based on the specified [dataTypes]. **This method exists for Java
     * interoperability only.**
     *
     * @param [date] should be the starting date from which the tide data is requested.
     * @param [days] should be the number of days for which tide data is requested.
     * @param [lat] is the latitude of the location for which the tides are requested.
     * @param [lon] is the longitude of the location for which the tides are requested.
     * @param [dataTypes] list of data types to request.
     * @param [callback] is the callback that will return either tide data or an error.
     */
    fun getTides(
            date: Date,
            days: Int,
            lat: String,
            lon: String,
            dataTypes: List<TideDataType>,
            callback: TidesCallback<Tides>
    ) {
        val dateStr = inputDateFormat.format(date)
        tidesRepository.tides(dataTypes, dateStr, days, lat, lon, apiKey) { result ->
            result.onFailure { callback.error(Error(it)) }
            result.onSuccess { callback.result(it) }
        }
    }
}
