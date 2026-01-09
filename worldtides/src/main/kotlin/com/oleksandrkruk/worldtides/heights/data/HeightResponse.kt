package com.oleksandrkruk.worldtides.heights.data

/**
 * DTO for parsing height data from JSON API response.
 */
internal data class HeightResponse(
    val dt: Long,
    val date: String,
    val height: Double
)
