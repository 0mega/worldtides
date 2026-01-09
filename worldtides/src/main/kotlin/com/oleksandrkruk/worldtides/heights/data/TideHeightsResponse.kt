package com.oleksandrkruk.worldtides.heights.data

/**
 * DTO for parsing tide heights response from the API.
 */
internal data class TideHeightsResponse(
    val status: Int,
    val error: String? = null,
    val heights: List<HeightResponse>
)
