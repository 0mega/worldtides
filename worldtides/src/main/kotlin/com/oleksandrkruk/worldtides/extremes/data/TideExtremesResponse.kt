package com.oleksandrkruk.worldtides.extremes.data

internal data class TideExtremesResponse(
    val status: Int,
    val error: String? = null,
    val extremes: List<ExtremeResponse>
)
