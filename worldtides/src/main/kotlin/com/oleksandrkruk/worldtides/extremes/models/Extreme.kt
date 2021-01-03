package com.oleksandrkruk.worldtides.extremes.models

import java.util.*

data class Extreme(
    val date: Date,
    val height: Float,
    val type: TideType
)
