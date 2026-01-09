package com.oleksandrkruk.worldtides.heights.models

import java.util.Date

/**
 * Represents a single tide height measurement at a specific time.
 *
 * @property date The timestamp of the prediction.
 * @property height The height level (datum relative).
 */
data class Height(
    val date: Date?,
    val height: Double
)
