package com.oleksandrkruk.worldtides

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*

class WorldTidesTest {
    @Test
    @DisplayName("returns valid object when provided with api key")
    fun returnsValidObjectWhenBuiltWithApiKey() {
        val worldTides = WorldTides.Builder().build("someKey")
        Assertions.assertNotNull(worldTides)
    }

    @Test
    @DisplayName("exposes method to get tide extremes")
    fun invokesRepository() {
        val builder = WorldTides.Builder()
        val worldTides = builder.build("someKey")
        val callbackMock = Mockito.mock(TidesCallback::class.java)
        worldTides.getTideExtremes(Date(), 5, "someLat", "someLon", callbackMock)
    }
}
