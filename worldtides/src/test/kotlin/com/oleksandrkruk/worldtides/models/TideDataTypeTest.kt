package com.oleksandrkruk.worldtides.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TideDataTypeTest {

    @Test
    @DisplayName("HEIGHTS has correct query value")
    fun heightsHasCorrectQueryValue() {
        assertEquals("heights", TideDataType.HEIGHTS.queryValue)
    }

    @Test
    @DisplayName("EXTREMES has correct query value")
    fun extremesHasCorrectQueryValue() {
        assertEquals("extremes", TideDataType.EXTREMES.queryValue)
    }

    @Test
    @DisplayName("TideDataType enum has exactly 2 values")
    fun tideDataTypeEnumHasExactlyTwoValues() {
        assertEquals(2, TideDataType.values().size)
    }

    @Test
    @DisplayName("TideDataType values contain HEIGHTS and EXTREMES")
    fun tideDataTypeValuesContainHeightsAndExtremes() {
        val values = TideDataType.values().toList()
        assertEquals(listOf(TideDataType.HEIGHTS, TideDataType.EXTREMES), values)
    }
}
