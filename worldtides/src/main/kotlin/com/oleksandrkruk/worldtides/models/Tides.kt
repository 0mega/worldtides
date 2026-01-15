package com.oleksandrkruk.worldtides.models

import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import com.oleksandrkruk.worldtides.heights.models.TideHeights

/**
 * Represents a response containing any combination of requested tide data types.
 *
 * @property heights Tide heights (null if not requested).
 * @property extremes Tide extremes (null if not requested).
 */
data class Tides(
    val heights: TideHeights? = null,
    val extremes: TideExtremes? = null
)
