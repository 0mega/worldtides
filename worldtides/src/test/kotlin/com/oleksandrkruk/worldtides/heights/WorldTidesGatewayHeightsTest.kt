package com.oleksandrkruk.worldtides.heights

import com.oleksandrkruk.worldtides.RetrofitClient
import com.oleksandrkruk.worldtides.WorldTidesGateway
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WorldTidesGatewayHeightsTest {
    private val server: MockWebServer = MockWebServer()
    private var service: WorldTidesGateway? = null

    private val heightsJsonResponse = """{
        "status": 200,
        "callCount": 1,
        "copyright": "Tidal data retrieved from www.worldtides.info...",
        "requestLat": 37.733299,
        "requestLon": -25.6667,
        "responseLat": 37.7333,
        "responseLon": -25.6667,
        "atlas": "FES",
        "station": "PONTA DELGADA",
        "heights": [
            {
                "dt": 1613540259,
                "date": "2021-02-17T05:37+0000",
                "height": 0.485
            },
            {
                "dt": 1613562548,
                "date": "2021-02-17T11:49+0000",
                "height": -0.425
            },
            {
                "dt": 1613584701,
                "date": "2021-02-17T17:58+0000",
                "height": 0.368
            }
        ]
    }"""

    @BeforeEach
    fun setup() {
        val response = MockResponse().setBody(heightsJsonResponse)
        server.enqueue(response)
        server.start()
        val baseUrl = server.url("")
        service = RetrofitClient(baseUrl.toString()).tidesService
    }

    @AfterEach
    fun teardown() {
        server.close()
    }

    @Test
    @DisplayName("heights endpoint contains 'heights' in query params")
    fun containsHeightsInQueryParams() {
        val response = service?.heights("foo", 1, "bar", "baz", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        assertTrue(requestUrl!!.queryParameterNames().contains("heights"))
    }

    @Test
    @DisplayName("parses three tide heights from mock JSON response")
    fun parsesThreeTideHeightsFromMockJsonResponse() {
        val response = service?.heights("foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(3, response.body()?.heights?.size)
    }

    @Test
    @DisplayName("parses tide height values from mock JSON response")
    fun parsesTideHeightValuesFromMockJsonResponse() {
        val response = service?.heights("foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(
            listOf(0.485, -0.425, 0.368),
            response.body()?.heights?.map { it.height }
        )
    }

    @Test
    @DisplayName("parses tide height dates from mock JSON response")
    fun parsesTideHeightDatesFromMockJsonResponse() {
        val response = service?.heights("foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(
            listOf(
                "2021-02-17T05:37+0000",
                "2021-02-17T11:49+0000",
                "2021-02-17T17:58+0000"
            ),
            response.body()?.heights?.map { it.date }
        )
    }

    @Test
    @DisplayName("parses tide height timestamps from mock JSON response")
    fun parsesTideHeightTimestampsFromMockJsonResponse() {
        val response = service?.heights("foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(
            listOf(1613540259L, 1613562548L, 1613584701L),
            response.body()?.heights?.map { it.dt }
        )
    }
}
