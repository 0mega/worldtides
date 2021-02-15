package com.oleksandrkruk.worldtides

import com.oleksandrkruk.worldtides.extremes.WorldTidesRepository
import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import java.text.SimpleDateFormat
import java.util.*

class WorldTides private constructor(
        private val apiKey: String,
        private val tidesRepository: WorldTidesRepository,
        private val inputDateFormat: SimpleDateFormat
    ) {

    class Builder {
        private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.US)
        private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val tidesGateway by lazy { RetrofitClient("https://www.worldtides.info/api/").tidesService }
        private val tidesRepository: WorldTidesRepository by lazy { WorldTidesRepository(tidesGateway, apiDateFormat) }

        /**
         * Create an instance of [WorldTides] using a given [apiKey].
         * The [apiKey] will be used for all the calls to the worldtides.info API.
         *
         * Note: You should avoid creating many instances of [WorldTides] as it is a costly operation. Your app
         * should cache an instance of [WorldTides] and reuse it for all the requests you make during
         * a user session.
         */
        fun build(
            apiKey: String
        ) : WorldTides {
            return WorldTides(apiKey, tidesRepository, inputDateFormat)
        }
    }

    /**
     * Returns only the extremes of the tides (Highs and Lows) between [date] and the number of [days] in future.
     *
     * @param [date] should be the starting date from which the tide extremes prediction are requested.
     * @param [days] should be the number of days for which tide extremes are requested. This number will add
     * to the [date] and will form a time range. E.g.: If the given [date] is 2020-12-01 and [days]
     * is 3 it means that the user will receive all tides between 2020-12-01 and 2020-12-03.
     * @param [lat] is the latitude of the location for which the tides are requested.
     * @param [lon] is the longitude of the location for which the tides are requested.
     * @param [callback] is the callback that will return the results. The [callback] will offer tide extremes
     * wrapped in a [Result]. When the operation is successful, the result will contain the tide extremes
     * otherwise it will provide an error.
     *
     */
    fun getTideExtremes(date: Date, days: Int, lat: String, lon: String, callback: (Result<TideExtremes>) -> Unit) {
        val dateStr = inputDateFormat.format(date)
        tidesRepository.extremes(dateStr, days, lat, lon, apiKey, callback)
    }

    /**
     * Returns only the extremes of the tides (Highs and Lows) between [date] and the number of [days] in future.
     * **This method exists for Java interoperability only.**
     *
     * @param [date] should be the starting date from which the tide extremes prediction are requested.
     * @param [days] should be the number of days for which tide extremes are requested. This number will add
     * to the [date] and will form a time range. E.g.: If the given [date] is 2020-12-01 and [days]
     * is 3 it means that the user will receive all tides between 2020-12-01 and 2020-12-03.
     * @param [lat] is the latitude of the location for which the tides are requested.
     * @param [lon] is the longitude of the location for which the tides are requested.
     * @param [callback] is the callback that will return either tide or an error.
     *
     */
    fun getTideExtremes(date: Date, days: Int, lat: String, lon: String, callback: TidesCallback) {
        val dateStr = inputDateFormat.format(date)
        tidesRepository.extremes(dateStr, days, lat, lon, apiKey) { result ->
            result.onFailure { callback.error(Error(it)) }
            result.onSuccess { callback.result(it) }
        }
    }
}
