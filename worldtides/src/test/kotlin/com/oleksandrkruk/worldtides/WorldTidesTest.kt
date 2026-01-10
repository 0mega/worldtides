package com.oleksandrkruk.worldtides

import com.oleksandrkruk.worldtides.models.TideDataType
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

    @Test
    @DisplayName("exposes method to get tide extremes")
    fun providesKotlinIdiomaticApiForExtremes() {
        val builder = WorldTides.Builder()
        val worldTides = builder.build("someKey")
        worldTides.getTideExtremes(Date(), 5, "someLat", "someLon") {}
    }

    @Test
    @DisplayName("exposes method to get tide heights")
    fun providesKotlinIdiomaticApiForHeights() {
        val builder = WorldTides.Builder()
        val worldTides = builder.build("someKey")
        worldTides.getTideHeights(Date(), 5, "someLat", "someLon") {}
    }

    @Test
    @DisplayName("exposes method to get flexible tides with data types")
    fun providesKotlinIdiomaticApiForFlexibleTides() {
        val builder = WorldTides.Builder()
        val worldTides = builder.build("someKey")
        worldTides.getTides(Date(), 5, "someLat", "someLon", listOf(TideDataType.HEIGHTS)) {}
    }

    @Test
    @DisplayName("exposes method to get flexible tides with multiple data types")
    fun providesKotlinIdiomaticApiForFlexibleTidesWithMultipleTypes() {
        val builder = WorldTides.Builder()
        val worldTides = builder.build("someKey")
        worldTides.getTides(Date(), 5, "someLat", "someLon", listOf(TideDataType.HEIGHTS, TideDataType.EXTREMES)) {}
    }

    @Test
    @DisplayName("exposes Java callback API for tide extremes")
    fun providesJavaCallbackApiForExtremes() {
        val worldTides = WorldTides.Builder().build("someKey")
        worldTides.getTideExtremes(Date(), 5, "someLat", "someLon", object : TidesCallback<com.oleksandrkruk.worldtides.extremes.models.TideExtremes> {
            override fun result(data: com.oleksandrkruk.worldtides.extremes.models.TideExtremes) {}
            override fun error(error: Error) {}
        })
    }

    @Test
    @DisplayName("exposes Java callback API for tide heights")
    fun providesJavaCallbackApiForHeights() {
        val worldTides = WorldTides.Builder().build("someKey")
        worldTides.getTideHeights(Date(), 5, "someLat", "someLon", object : TidesCallback<com.oleksandrkruk.worldtides.heights.models.TideHeights> {
            override fun result(data: com.oleksandrkruk.worldtides.heights.models.TideHeights) {}
            override fun error(error: Error) {}
        })
    }

    @Test
    @DisplayName("exposes Java callback API for flexible tides")
    fun providesJavaCallbackApiForFlexibleTides() {
        val worldTides = WorldTides.Builder().build("someKey")
        worldTides.getTides(Date(), 5, "someLat", "someLon", listOf(TideDataType.HEIGHTS, TideDataType.EXTREMES), object : TidesCallback<com.oleksandrkruk.worldtides.models.Tides> {
            override fun result(data: com.oleksandrkruk.worldtides.models.Tides) {}
            override fun error(error: Error) {}
        })
    }
}

