package com.oleksandrkruk.worldtides.heights.models

/**
 * Wrapper for the list of tide heights.
 *
 * @property heights Collection of predicted tide heights.
 */
data class TideHeights(
    val heights: List<Height>
)
