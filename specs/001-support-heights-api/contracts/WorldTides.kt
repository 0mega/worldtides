package com.oleksandrkruk.worldtides

import com.oleksandrkruk.worldtides.heights.models.TideHeights
import com.oleksandrkruk.worldtides.models.Tides
import com.oleksandrkruk.worldtides.models.TideDataType
import java.util.Date

// This contract defines the changes to the public API
class WorldTides {

    // ... existing properties and constructor ...

    /**
     * Returns the predicted tide heights between [date] and number of [days] in future.
     *
     * @param date starting date
     * @param days number of days (duration)
     * @param lat latitude
     * @param lon longitude
     * @param callback result handler (Kotlin lambda)
     */
    fun getTideHeights(
        date: Date,
        days: Int,
        lat: String,
        lon: String,
        callback: (Result<TideHeights>) -> Unit
    ) {
        // Implementation
    }

    /**
     * Java-Compatible overload for tide heights.
     */
    fun getTideHeights(
        date: Date,
        days: Int,
        lat: String,
        lon: String,
        callback: TidesCallback<TideHeights>
    ) {
        // Implementation
    }

    /**
     * Returns tide data based on the specified [dataTypes].
     * Supports stacking multiple data types in a single API call.
     *
     * @param date starting date
     * @param days number of days (duration)
     * @param lat latitude
     * @param lon longitude
     * @param dataTypes list of data types to request (e.g., HEIGHTS, EXTREMES)
     * @param callback result handler (Kotlin lambda)
     */
    fun getTides(
        date: Date,
        days: Int,
        lat: String,
        lon: String,
        dataTypes: List<TideDataType>,
        callback: (Result<Tides>) -> Unit
    ) {
        // Implementation
    }

    /**
     * Java-Compatible overload for flexible tide data.
     */
    fun getTides(
        date: Date,
        days: Int,
        lat: String,
        lon: String,
        dataTypes: List<TideDataType>,
        callback: TidesCallback<Tides>
    ) {
        // Implementation
    }
}

/**
 * Enum representing the types of tide data that can be requested.
 * Future versions will add STATIONS, DATUMS, etc.
 */
enum class TideDataType {
    HEIGHTS,
    EXTREMES
    // Future: STATIONS, DATUMS
}
