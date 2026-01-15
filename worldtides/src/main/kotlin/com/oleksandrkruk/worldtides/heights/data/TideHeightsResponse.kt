package com.oleksandrkruk.worldtides.heights.data

import com.oleksandrkruk.worldtides.heights.models.TideHeights
import java.text.SimpleDateFormat

/**
 * DTO for parsing tide heights response from the API.
 */
internal data class TideHeightsResponse(
    val status: Int,
    val error: String? = null,
    val heights: List<HeightResponse>
)

internal fun TideHeightsResponse.toTideHeights(dateFormat: SimpleDateFormat) = TideHeights(
    heights = heights.map { it.toHeight(dateFormat) }
)
