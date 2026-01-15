package com.oleksandrkruk.worldtides.models

/**
 * Enum representing the types of tide data that can be requested.
 * Used with [getTides] to specify which data types to fetch in a single API call.
 */
enum class TideDataType(val queryValue: String) {
    HEIGHTS("heights"),
    EXTREMES("extremes")
    // Future: STATIONS("stations"), DATUMS("datums")
}
