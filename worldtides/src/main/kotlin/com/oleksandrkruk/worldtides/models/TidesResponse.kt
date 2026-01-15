package com.oleksandrkruk.worldtides.models

import com.oleksandrkruk.worldtides.extremes.data.ExtremeResponse
import com.oleksandrkruk.worldtides.heights.data.HeightResponse

/**
 * DTO for parsing combined/flexible tide response from the API.
 */
internal data class TidesResponse(
    val status: Int,
    val error: String? = null,
    val heights: List<HeightResponse>? = null,
    val extremes: List<ExtremeResponse>? = null
)
