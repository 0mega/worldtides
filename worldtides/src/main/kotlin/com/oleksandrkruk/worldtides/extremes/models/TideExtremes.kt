package com.oleksandrkruk.worldtides.extremes.models

import java.util.*

data class TideExtremes(
    val extremes: List<Extreme>,
    // All DateTime objects for tide extremes are returned for this TimeZone
    val timeZone: TimeZone
)
