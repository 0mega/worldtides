package com.oleksandrkruk.worldtides.heights.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class HeightTest {

    @Test
    @DisplayName("Height stores date correctly")
    fun heightStoresDateCorrectly() {
        val testDate = Date()
        val height = Height(testDate, 0.485)
        assertEquals(testDate, height.date)
    }

    @Test
    @DisplayName("Height stores height value correctly")
    fun heightStoresHeightValueCorrectly() {
        val height = Height(Date(), 0.485)
        assertEquals(0.485, height.height)
    }

    @Test
    @DisplayName("Height handles null date")
    fun heightHandlesNullDate() {
        val height = Height(null, 0.485)
        assertNull(height.date)
    }

    @Test
    @DisplayName("Height handles negative height values")
    fun heightHandlesNegativeHeightValues() {
        val height = Height(Date(), -0.425)
        assertEquals(-0.425, height.height)
    }

    @Test
    @DisplayName("Height handles zero height value")
    fun heightHandlesZeroHeightValue() {
        val height = Height(Date(), 0.0)
        assertEquals(0.0, height.height)
    }
}
