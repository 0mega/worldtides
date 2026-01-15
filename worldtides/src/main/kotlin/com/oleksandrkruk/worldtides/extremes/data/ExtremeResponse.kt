package com.oleksandrkruk.worldtides.extremes.data

import com.oleksandrkruk.worldtides.extremes.models.Extreme
import com.oleksandrkruk.worldtides.extremes.models.TideType
import java.text.SimpleDateFormat

internal data class ExtremeResponse(val dt: Long, val date: String, val height: Float, val type: String)

internal fun ExtremeResponse.toExtreme(dateFormat: SimpleDateFormat) = Extreme(
    date = dateFormat.parse(date)!!,
    height = height,
    type = TideType.valueOf(type)
)
