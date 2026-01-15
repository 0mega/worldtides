package com.oleksandrkruk.worldtides.models

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

class WorldTidesGatewayTidesTest {
    private val server: MockWebServer = MockWebServer()
    private var service: WorldTidesGateway? = null

    private val combinedJsonResponse = """{
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
            }
        ],
        "extremes": [
            {
                "dt": 1613540259,
                "date": "2021-02-17T05:37+0000",
                "height": 0.485,
                "type": "High"
            },
            {
                "dt": 1613562548,
                "date": "2021-02-17T11:49+0000",
                "height": -0.425,
                "type": "Low"
            }
        ]
    }"""

    @BeforeEach
    fun setup() {
        val response = MockResponse().setBody(combinedJsonResponse)
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
    @DisplayName("tides endpoint uses dynamic URL with heights and extremes")
    fun tidesEndpointUsesDynamicUrl() {
        val response = service?.tides("v2?heights&extremes", "2021-02-17", 1, "37.7333", "-25.6667", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        assertTrue(requestUrl!!.queryParameterNames().contains("heights"))
        assertTrue(requestUrl.queryParameterNames().contains("extremes"))
    }

    @Test
    @DisplayName("parses both heights and extremes from combined response")
    fun parsesBothHeightsAndExtremesFromCombinedResponse() {
        val response = service?.tides("v2?heights&extremes", "foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(2, response.body()?.heights?.size)
        assertEquals(2, response.body()?.extremes?.size)
    }

    @Test
    @DisplayName("parses height values from combined response")
    fun parsesHeightValuesFromCombinedResponse() {
        val response = service?.tides("v2?heights&extremes", "foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(
            listOf(0.485, -0.425),
            response.body()?.heights?.map { it.height }
        )
    }

    @Test
    @DisplayName("parses extremes types from combined response")
    fun parsesExtremesTypesFromCombinedResponse() {
        val response = service?.tides("v2?heights&extremes", "foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(
            listOf("High", "Low"),
            response.body()?.extremes?.map { it.type }
        )
    }
}
