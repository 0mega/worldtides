package com.oleksandrkruk.worldtides.heights.data

import com.oleksandrkruk.worldtides.heights.models.Height
import java.text.SimpleDateFormat

/**
 * DTO for parsing height data from JSON API response.
 */
internal data class HeightResponse(
    val dt: Long,
    val date: String,
    val height: Double
)

internal fun HeightResponse.toHeight(dateFormat: SimpleDateFormat) = Height(
    date = dateFormat.parse(date)!!,
    height = height
)
