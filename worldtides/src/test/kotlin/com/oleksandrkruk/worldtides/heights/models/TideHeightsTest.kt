package com.oleksandrkruk.worldtides.heights.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class TideHeightsTest {

    @Test
    @DisplayName("TideHeights stores list of heights correctly")
    fun tideHeightsStoresListOfHeightsCorrectly() {
        val heights = listOf(
            Height(Date(), 0.485),
            Height(Date(), -0.425),
            Height(Date(), 0.368)
        )
        val tideHeights = TideHeights(heights)
        assertEquals(3, tideHeights.heights.size)
    }

    @Test
    @DisplayName("TideHeights handles empty list")
    fun tideHeightsHandlesEmptyList() {
        val tideHeights = TideHeights(emptyList())
        assertTrue(tideHeights.heights.isEmpty())
    }

    @Test
    @DisplayName("TideHeights preserves height order")
    fun tideHeightsPreservesHeightOrder() {
        val heights = listOf(
            Height(Date(), 0.485),
            Height(Date(), -0.425),
            Height(Date(), 0.368)
        )
        val tideHeights = TideHeights(heights)
        assertEquals(listOf(0.485, -0.425, 0.368), tideHeights.heights.map { it.height })
    }

    @Test
    @DisplayName("TideHeights is data class with correct equals")
    fun tideHeightsIsDataClassWithCorrectEquals() {
        val testDate = Date()
        val heights1 = TideHeights(listOf(Height(testDate, 0.5)))
        val heights2 = TideHeights(listOf(Height(testDate, 0.5)))
        assertEquals(heights1, heights2)
    }
}
