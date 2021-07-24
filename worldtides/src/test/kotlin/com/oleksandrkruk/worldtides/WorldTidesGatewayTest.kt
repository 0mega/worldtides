package com.oleksandrkruk.worldtides

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WorldTidesGatewayTest {
    private val server: MockWebServer = MockWebServer()
    private var service: WorldTidesGateway? = null

    @BeforeEach
    fun setup() {
        val response = MockResponse().setBody("{\n" +
                "  \"status\": 200,\n" +
                "  \"callCount\": 1,\n" +
                "  \"copyright\": \"Tidal data retrieved from www.worldtides.info...\",\n" +
                "  \"requestLat\": 37.733299,\n" +
                "  \"requestLon\": -25.6667,\n" +
                "  \"responseLat\": 37.7333,\n" +
                "  \"responseLon\": -25.6667,\n" +
                "  \"atlas\": \"FES\",\n" +
                "  \"station\": \"PONTA DELGADA\",\n" +
                "  \"extremes\": [\n" +
                "    {\n" +
                "      \"dt\": 1613540259,\n" +
                "      \"date\": \"2021-02-17T05:37+0000\",\n" +
                "      \"height\": 0.485,\n" +
                "      \"type\": \"High\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"dt\": 1613562548,\n" +
                "      \"date\": \"2021-02-17T11:49+0000\",\n" +
                "      \"height\": -0.425,\n" +
                "      \"type\": \"Low\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"dt\": 1613584701,\n" +
                "      \"date\": \"2021-02-17T17:58+0000\",\n" +
                "      \"height\": 0.368,\n" +
                "      \"type\": \"High\"\n" +
                "    }]}")
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
        Assert.assertTrue(requestUrl!!.queryParameterNames().contains("extremes"))
    }

    @Test
    fun containsDateInQueryParams() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        Assert.assertTrue(requestUrl!!.queryParameterNames().contains("date"))
        Assert.assertEquals("foo", requestUrl.queryParameter("date"))
    }

    @Test
    fun containsDaysInQueryParams() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        Assert.assertTrue(requestUrl!!.queryParameterNames().contains("days"))
        Assert.assertEquals("1", requestUrl.queryParameter("days"))
    }

    @Test
    fun containsLatInQueryParams() {
        val response = service?.extremes("foo", 1, "25.134", "baz", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        Assert.assertTrue(requestUrl!!.queryParameterNames().contains("lat"))
        Assert.assertEquals("25.134", requestUrl.queryParameter("lat"))
    }

    @Test
    fun containsLonInQueryParams() {
        val response = service?.extremes("foo", 1, "bar", "13.948", "key")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        Assert.assertTrue(requestUrl!!.queryParameterNames().contains("lon"))
        Assert.assertEquals("13.948", requestUrl.queryParameter("lon"))
    }

    @Test
    fun containsApiKeyInQueryParams() {
        val response = service?.extremes("foo", 1, "bar", "baz", "someKey")?.execute()
        val requestUrl = response?.raw()?.request()?.url()
        Assert.assertTrue(requestUrl!!.queryParameterNames().contains("key"))
        Assert.assertEquals("someKey", requestUrl.queryParameter("key"))
    }

    @Test
    fun parsesThreeTideExtremesFromMockJsonResponse() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        Assert.assertTrue(response!!.isSuccessful)
        Assert.assertEquals(3, response.body()?.extremes?.size)
    }

    @Test
    fun parsesTideExtremeTypesFromMockJsonResponse() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        Assert.assertTrue(response!!.isSuccessful)
        Assert.assertEquals(
            listOf("High", "Low", "High"),
            response.body()?.extremes?.map { extreme -> extreme.type })
    }

    @Test
    fun parsesTideExtremeDatesFromMockJsonResponse() {
        val response = service?.extremes("foo", 1, "bar", "baz", "key")?.execute()
        Assert.assertTrue(response!!.isSuccessful)
        Assert.assertEquals(listOf(
            "2021-02-17T05:37+0000",
            "2021-02-17T11:49+0000",
            "2021-02-17T17:58+0000"),
            response.body()?.extremes?.map { extreme -> extreme.date })
    }
}
