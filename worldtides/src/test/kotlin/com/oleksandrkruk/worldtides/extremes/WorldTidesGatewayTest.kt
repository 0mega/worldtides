package com.oleksandrkruk.worldtides.extremes

import com.oleksandrkruk.worldtides.RetrofitClient
import com.oleksandrkruk.worldtides.WorldTidesGateway
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WorldTidesGatewayTest {
    private val server: MockWebServer = MockWebServer()
    private var service: WorldTidesGateway? = null

    @BeforeEach
    fun setup() {
        val response = MockResponse().setBody("""{
            "status": 200,
            "callCount": 1,
            "copyright": "Tidal data retrieved from www.worldtides.info...",
            "requestLat": 37.733299,
            "requestLon": -25.6667,
            "responseLat": 37.7333,
            "responseLon": -25.6667,
            "atlas": "FES",
            "station": "PONTA DELGADA",
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
                },
                {
                    "dt": 1613584701,
                    "date": "2021-02-17T17:58+0000",
                    "height": 0.368,
                    "type": "High"
                }
            ]
        }""")
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
    fun containsExtremesInQueryParams() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        assertTrue(requestUrl!!.queryParameterNames().contains("extremes"))
    }

    @Test
    fun containsDateInQueryParams() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        assertTrue(requestUrl!!.queryParameterNames().contains("date"))
        assertEquals("foo", requestUrl.queryParameter("date"))
    }

    @Test
    fun containsDaysInQueryParams() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        assertTrue(requestUrl!!.queryParameterNames().contains("days"))
        assertEquals("1", requestUrl.queryParameter("days"))
    }

    @Test
    fun containsLatInQueryParams() {
        val response = service?.extremes("foo", 1, "25.134", "baz", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        assertTrue(requestUrl!!.queryParameterNames().contains("lat"))
        assertEquals("25.134", requestUrl.queryParameter("lat"))
    }

    @Test
    fun containsLonInQueryParams() {
        val response = service?.extremes("foo", 1, "bar", "13.948", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        assertTrue(requestUrl!!.queryParameterNames().contains("lon"))
        assertEquals("13.948", requestUrl.queryParameter("lon"))
    }

    @Test
    fun containsApiKeyInQueryParams() {
        val response = service?.extremes("foo", 1, "bar", "baz", "someKey")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        assertTrue(requestUrl!!.queryParameterNames().contains("key"))
        assertEquals("someKey", requestUrl.queryParameter("key"))
    }

    @Test
    fun parsesThreeTideExtremesFromMockJsonResponse() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(3, response.body()?.extremes?.size)
    }

    @Test
    fun parsesTideExtremeTypesFromMockJsonResponse() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(
            listOf("High", "Low", "High"),
            response.body()?.extremes?.map { extreme -> extreme.type })
    }

    @Test
    fun parsesTideExtremeDatesFromMockJsonResponse() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        assertTrue(response!!.isSuccessful)
        assertEquals(listOf(
            "2021-02-17T05:37+0000",
            "2021-02-17T11:49+0000",
            "2021-02-17T17:58+0000"),
            response.body()?.extremes?.map { extreme -> extreme.date })
    }
}
