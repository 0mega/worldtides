package com.oleksandrkruk.worldtides.models

import com.oleksandrkruk.worldtides.extremes.models.Extreme
import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import com.oleksandrkruk.worldtides.extremes.models.TideType
import com.oleksandrkruk.worldtides.heights.models.Height
import com.oleksandrkruk.worldtides.heights.models.TideHeights
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class TidesTest {

    @Test
    @DisplayName("Tides stores both heights and extremes")
    fun tidesStoresBothHeightsAndExtremes() {
        val heights = TideHeights(listOf(Height(Date(), 0.485)))
        val extremes = TideExtremes(listOf(Extreme(Date(), 0.485f, TideType.High)))
        val tides = Tides(heights = heights, extremes = extremes)
        
        assertNotNull(tides.heights)
        assertNotNull(tides.extremes)
        assertEquals(1, tides.heights?.heights?.size)
        assertEquals(1, tides.extremes?.extremes?.size)
    }

    @Test
    @DisplayName("Tides handles null heights")
    fun tidesHandlesNullHeights() {
        val extremes = TideExtremes(listOf(Extreme(Date(), 0.485f, TideType.High)))
        val tides = Tides(heights = null, extremes = extremes)
        
        assertNull(tides.heights)
        assertNotNull(tides.extremes)
    }

    @Test
    @DisplayName("Tides handles null extremes")
    fun tidesHandlesNullExtremes() {
        val heights = TideHeights(listOf(Height(Date(), 0.485)))
        val tides = Tides(heights = heights, extremes = null)
        
        assertNotNull(tides.heights)
        assertNull(tides.extremes)
    }

    @Test
    @DisplayName("Tides handles both null")
    fun tidesHandlesBothNull() {
        val tides = Tides(heights = null, extremes = null)
        
        assertNull(tides.heights)
        assertNull(tides.extremes)
    }

    @Test
    @DisplayName("Tides default constructor sets nulls")
    fun tidesDefaultConstructorSetsNulls() {
        val tides = Tides()
        
        assertNull(tides.heights)
        assertNull(tides.extremes)
    }

    @Test
    @DisplayName("Tides is data class with correct equals")
    fun tidesIsDataClassWithCorrectEquals() {
        val heights = TideHeights(listOf(Height(null, 0.5)))
        val tides1 = Tides(heights = heights, extremes = null)
        val tides2 = Tides(heights = heights, extremes = null)
        assertEquals(tides1, tides2)
    }
}
