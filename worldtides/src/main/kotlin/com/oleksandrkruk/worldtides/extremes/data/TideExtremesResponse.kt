package com.oleksandrkruk.worldtides.extremes.data

import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import java.text.SimpleDateFormat

internal data class TideExtremesResponse(
    val status: Int,
    val error: String? = null,
    val extremes: List<ExtremeResponse>
)

internal fun TideExtremesResponse.toTideExtremes(dateFormat: SimpleDateFormat) = TideExtremes(
    extremes = extremes.map { it.toExtreme(dateFormat) }
)
