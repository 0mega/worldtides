package com.oleksandrkruk.worldtides.extremes.data
import com.oleksandrkruk.worldtides.WorldTidesGateway

internal data class TideExtremesResponse(
    val status: Int,
    val error: String? = null,
    val extremes: List<ExtremeResponse>,
    // Set as full timezone name (ex. America/Los_Angeles) if localTime is not set to null
    val timezone: String?
)
