package com.oleksandrkruk.worldtides

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class WorldTidesTest {
    @Test
    @DisplayName("returns valid object when provided with api key")
    fun returnsValidObjectWhenBuiltWithApiKey() {
        val worldTides = WorldTides.Builder().build("someKey")
        Assertions.assertNotNull(worldTides)
    }

    // TODO this is not an optimal test. Need to improve it by testing the invocation of the dependencies.
    @Test
    @DisplayName("exposes method to get tide extremes")
    fun providesKotlinIdiomaticApiForExtremes() {
        val builder = WorldTides.Builder()
        val worldTides = builder.build("someKey")
        worldTides.getTideExtremes(Date(), 5, "someLat", "someLon") {}
    }
}
