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
}
