package com.oleksandrkruk.worldtides.heights.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

class HeightResponseMappingTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.US)

    @Test
    @DisplayName("HeightResponse.toHeight() maps all fields correctly")
    fun toHeightMapsAllFieldsCorrectly() {
        val response = HeightResponse(
            dt = 1613540259L,
            date = "2021-02-17T05:37+0000",
            height = 0.485
        )
        
        val height = response.toHeight(dateFormat)
        
        assertEquals(dateFormat.parse("2021-02-17T05:37+0000"), height.date)
        assertEquals(0.485, height.height)
    }

    @Test
    @DisplayName("HeightResponse.toHeight() handles negative height")
    fun toHeightHandlesNegativeHeight() {
        val response = HeightResponse(
            dt = 1613540259L,
            date = "2021-02-17T11:45+0000",
            height = -0.425
        )
        
        val height = response.toHeight(dateFormat)
        
        assertEquals(-0.425, height.height)
    }

    @Test
    @DisplayName("TideHeightsResponse.toTideHeights() maps list correctly")
    fun toTideHeightsMapsListCorrectly() {
        val response = TideHeightsResponse(
            status = 200,
            error = null,
            heights = listOf(
                HeightResponse(1L, "2021-02-17T05:37+0000", 0.485),
                HeightResponse(2L, "2021-02-17T11:45+0000", -0.425)
            )
        )
        
        val tideHeights = response.toTideHeights(dateFormat)
        
        assertEquals(2, tideHeights.heights.size)
        assertEquals(0.485, tideHeights.heights[0].height)
        assertEquals(-0.425, tideHeights.heights[1].height)
    }

    @Test
    @DisplayName("TideHeightsResponse.toTideHeights() handles empty list")
    fun toTideHeightsHandlesEmptyList() {
        val response = TideHeightsResponse(
            status = 200,
            error = null,
            heights = emptyList()
        )
        
        val tideHeights = response.toTideHeights(dateFormat)
        
        assertEquals(0, tideHeights.heights.size)
    }
}
