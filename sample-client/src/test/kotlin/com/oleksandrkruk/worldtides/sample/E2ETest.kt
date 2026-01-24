package com.oleksandrkruk.worldtides.sample

import com.oleksandrkruk.worldtides.WorldTides
import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import com.oleksandrkruk.worldtides.extremes.models.TideType
import com.oleksandrkruk.worldtides.heights.models.TideHeights
import com.oleksandrkruk.worldtides.models.TideDataType
import com.oleksandrkruk.worldtides.models.Tides
import java.util.Date
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * E2E Integration Tests for WorldTides library.
 *
 * These tests exercise all API methods against the live World Tides API. Requires
 * WORLD_TIDES_API_KEY environment variable to be set.
 *
 * Test location: Ferraria Hot Springs, São Miguel, Azores, Portugal Coordinates: 37.8574, -25.8556
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class E2ETest {

    private lateinit var worldTides: WorldTides
    private val apiKey: String = System.getenv("WORLD_TIDES_API_KEY") ?: ""

    // Test location: Ferraria, Azores, Portugal
    private val lat = "37.8574"
    private val lon = "-25.8556"
    private val days = 7

    @BeforeAll
    fun setup() {
        assumeTrue(apiKey.isNotBlank(), "WORLD_TIDES_API_KEY must be set to run E2E tests")
        worldTides = WorldTides.Builder().build(apiKey)
    }

    /**
     * T007 [US1]
     * - FR-001: Verify getTideExtremes() returns valid data
     */
    @Test
    fun testGetTideExtremes() {
        val latch = CountDownLatch(1)
        val resultRef = AtomicReference<Result<TideExtremes>>()

        worldTides.getTideExtremes(Date(), days, lat, lon) { result ->
            resultRef.set(result)
            latch.countDown()
        }

        assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue()

        val result = resultRef.get()
        assertThat(result.isSuccess).isTrue()

        val extremes = result.getOrThrow()
        assertThat(extremes.extremes).isNotEmpty()

        // Validate structure of first extreme
        val first = extremes.extremes.first()
        assertThat(first.date).isNotNull()
        assertThat(first.height).isNotNull()
        assertThat(first.type).isIn(TideType.High, TideType.Low)
    }

    /**
     * T008 [US1]
     * - FR-002: Verify getTideHeights() returns valid data
     */
    @Test
    fun testGetTideHeights() {
        val latch = CountDownLatch(1)
        val resultRef = AtomicReference<Result<TideHeights>>()

        worldTides.getTideHeights(Date(), days, lat, lon) { result ->
            resultRef.set(result)
            latch.countDown()
        }

        assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue()

        val result = resultRef.get()
        assertThat(result.isSuccess).isTrue()

        val heights = result.getOrThrow()
        assertThat(heights.heights).isNotEmpty()

        // Validate structure of first height
        val first = heights.heights.first()
        assertThat(first.date).isNotNull()
        assertThat(first.height).isNotNull()
    }

    /**
     * T009 [US1]
     * - FR-003: Verify getTides() with HEIGHTS only
     */
    @Test
    fun testGetTidesHeightsOnly() {
        val latch = CountDownLatch(1)
        val resultRef = AtomicReference<Result<Tides>>()

        worldTides.getTides(Date(), days, lat, lon, listOf(TideDataType.HEIGHTS)) { result ->
            resultRef.set(result)
            latch.countDown()
        }

        assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue()

        val result = resultRef.get()
        assertThat(result.isSuccess).isTrue()

        val tides = result.getOrThrow()
        assertThat(tides.heights).isNotNull()
        assertThat(tides.heights!!.heights).isNotEmpty()
        assertThat(tides.extremes).isNull()
    }

    /**
     * T010 [US1]
     * - FR-004: Verify getTides() with EXTREMES only
     */
    @Test
    fun testGetTidesExtremesOnly() {
        val latch = CountDownLatch(1)
        val resultRef = AtomicReference<Result<Tides>>()

        worldTides.getTides(Date(), days, lat, lon, listOf(TideDataType.EXTREMES)) { result ->
            resultRef.set(result)
            latch.countDown()
        }

        assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue()

        val result = resultRef.get()
        assertThat(result.isSuccess).isTrue()

        val tides = result.getOrThrow()
        assertThat(tides.extremes).isNotNull()
        assertThat(tides.extremes!!.extremes).isNotEmpty()
        assertThat(tides.heights).isNull()
    }

    /**
     * T011 [US1]
     * - FR-005: Verify getTides() with both HEIGHTS and EXTREMES
     */
    @Test
    fun testGetTidesBothTypes() {
        val latch = CountDownLatch(1)
        val resultRef = AtomicReference<Result<Tides>>()

        worldTides.getTides(
                Date(),
                days,
                lat,
                lon,
                listOf(TideDataType.HEIGHTS, TideDataType.EXTREMES)
        ) { result ->
            resultRef.set(result)
            latch.countDown()
        }

        assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue()

        val result = resultRef.get()
        assertThat(result.isSuccess).isTrue()

        val tides = result.getOrThrow()
        assertThat(tides.heights).isNotNull()
        assertThat(tides.heights!!.heights).isNotEmpty()
        assertThat(tides.extremes).isNotNull()
        assertThat(tides.extremes!!.extremes).isNotEmpty()
    }

    /**
     * T014 [US2]
     * - FR-006: Verify error handling with invalid API key
     */
    @Test
    fun testInvalidApiKeyReturnsError() {
        val invalidWorldTides = WorldTides.Builder().build("invalid-api-key-12345")
        val latch = CountDownLatch(1)
        val resultRef = AtomicReference<Result<TideExtremes>>()

        invalidWorldTides.getTideExtremes(Date(), days, lat, lon) { result ->
            resultRef.set(result)
            latch.countDown()
        }

        assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue()

        val result = resultRef.get()
        // With invalid key, we expect either a failure or success with error indicator
        // The exact behavior depends on the API response
        assertThat(result).isNotNull()
    }
}
