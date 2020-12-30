package com.oleksandrkruk.worldtides

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class WorldTidesTest {
    @Test
    fun testMethod() {
        val classUnderTest = WorldTides()
        assertTrue(classUnderTest.test(), "test method should return 'true'")
    }
}
