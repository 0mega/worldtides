package com.oleksandrkruk.worldtides.heights

import com.oleksandrkruk.worldtides.WorldTidesGateway
import com.oleksandrkruk.worldtides.WorldTidesRepository
import com.oleksandrkruk.worldtides.heights.data.HeightResponse
import com.oleksandrkruk.worldtides.heights.data.TideHeightsResponse
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.fail

@Suppress("UNCHECKED_CAST")
class WorldTidesRepositoryHeightsTest {

    @Mock
    private lateinit var gatewayMock: WorldTidesGateway
    @Mock
    lateinit var dateFormatterMock: SimpleDateFormat
    @Mock
    private lateinit var mockCall: Call<TideHeightsResponse>

    private lateinit var heightsResponse: TideHeightsResponse
    private lateinit var tidesRepository: WorldTidesRepository

    private val today = Date()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(dateFormatterMock.parse(ArgumentMatchers.any())).thenReturn(today)
        tidesRepository = WorldTidesRepository(gatewayMock, dateFormatterMock)
    }

    @Test
    @DisplayName("maps correctly from height data to model on success")
    fun mapsCorrectlyFromDataToModel() {
        withSuccessfulResponse()
        heightsResponse = TideHeightsResponse(200, null, listOf(buildHeightData()))

        tidesRepository.heights("", 1, "", "", "") { result ->
            assertTrue(result.isSuccess)
            result.onSuccess { tideHeights ->
                assertEquals(1, tideHeights.heights.size)
                assertEquals(today.toString(), tideHeights.heights.first().date.toString())
                assertEquals(0.485, tideHeights.heights.first().height)
            }
            result.onFailure {
                fail("should never invoke failure on successful response")
            }
        }
    }

    @Test
    @DisplayName("maps multiple heights correctly")
    fun mapsMultipleHeightsCorrectly() {
        withSuccessfulResponse()
        heightsResponse = TideHeightsResponse(
            200, null, listOf(
                buildHeightData(height = 0.485),
                buildHeightData(height = -0.425),
                buildHeightData(height = 0.368)
            )
        )

        tidesRepository.heights("", 1, "", "", "") { result ->
            assertTrue(result.isSuccess)
            result.onSuccess { tideHeights ->
                assertEquals(3, tideHeights.heights.size)
                assertEquals(listOf(0.485, -0.425, 0.368), tideHeights.heights.map { it.height })
            }
        }
    }

    @Test
    @DisplayName("bubbles up error on failed response")
    fun bubblesUpTheErrorOnFailure() {
        withFailedResponse()
        tidesRepository.heights("", 1, "", "", "") { result ->
            assertTrue(result.isFailure)
            result.onSuccess { _ ->
                fail("should never invoke success on failed response")
            }
            result.onFailure {
                assertEquals(Error::class, it::class)
            }
        }
    }

    @Test
    @DisplayName("handles empty heights list")
    fun handlesEmptyHeightsList() {
        withSuccessfulResponse()
        heightsResponse = TideHeightsResponse(200, null, emptyList())

        tidesRepository.heights("", 1, "", "", "") { result ->
            assertTrue(result.isSuccess)
            result.onSuccess { tideHeights ->
                assertEquals(0, tideHeights.heights.size)
            }
        }
    }

    private fun buildHeightData(
        dt: Long = 1613540259L,
        date: String = "2021-02-17T05:37+0000",
        height: Double = 0.485
    ): HeightResponse {
        return HeightResponse(dt, date, height)
    }

    private fun withSuccessfulResponse() {
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<TideHeightsResponse> = invocation.arguments[0] as Callback<TideHeightsResponse>
            val success = Response.success(200, heightsResponse)
            callback.onResponse(mockCall, success)
            null
        }
        `when`(gatewayMock.heights(anyString(), anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(mockCall)
    }

    private fun withFailedResponse() {
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<TideHeightsResponse> = invocation.arguments[0] as Callback<TideHeightsResponse>
            val mockResponseBody = ResponseBody.create(MediaType.get("json/txt"), "")
            val timeout = Response.error<TideHeightsResponse>(408, mockResponseBody)
            callback.onResponse(mockCall, timeout)
            null
        }
        `when`(gatewayMock.heights(anyString(), anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(mockCall)
    }
}
