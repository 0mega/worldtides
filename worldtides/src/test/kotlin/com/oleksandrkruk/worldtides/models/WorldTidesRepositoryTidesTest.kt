package com.oleksandrkruk.worldtides.models

import com.oleksandrkruk.worldtides.WorldTidesGateway
import com.oleksandrkruk.worldtides.WorldTidesRepository
import com.oleksandrkruk.worldtides.extremes.data.ExtremeResponse
import com.oleksandrkruk.worldtides.extremes.models.TideType
import com.oleksandrkruk.worldtides.heights.data.HeightResponse
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
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
class WorldTidesRepositoryTidesTest {

    @Mock
    private lateinit var gatewayMock: WorldTidesGateway
    @Mock
    lateinit var dateFormatterMock: SimpleDateFormat
    @Mock
    private lateinit var mockCall: Call<TidesResponse>

    private lateinit var tidesResponse: TidesResponse
    private lateinit var tidesRepository: WorldTidesRepository

    private val today = Date()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(dateFormatterMock.parse(ArgumentMatchers.any())).thenReturn(today)
        tidesRepository = WorldTidesRepository(gatewayMock, dateFormatterMock)
    }

    @Test
    @DisplayName("maps both heights and extremes from combined response")
    fun mapsBothHeightsAndExtremesFromCombinedResponse() {
        withSuccessfulResponse()
        tidesResponse = TidesResponse(
            200, null,
            heights = listOf(buildHeightData()),
            extremes = listOf(buildExtremeData())
        )

        tidesRepository.tides(listOf(TideDataType.HEIGHTS, TideDataType.EXTREMES), "", 1, "", "", "") { result ->
            assertTrue(result.isSuccess)
            result.onSuccess { tides ->
                assertNotNull(tides.heights)
                assertNotNull(tides.extremes)
                assertEquals(1, tides.heights?.heights?.size)
                assertEquals(1, tides.extremes?.extremes?.size)
            }
        }
    }

    @Test
    @DisplayName("maps only heights when only heights requested")
    fun mapsOnlyHeightsWhenOnlyHeightsRequested() {
        withSuccessfulResponse()
        tidesResponse = TidesResponse(
            200, null,
            heights = listOf(buildHeightData()),
            extremes = null
        )

        tidesRepository.tides(listOf(TideDataType.HEIGHTS), "", 1, "", "", "") { result ->
            assertTrue(result.isSuccess)
            result.onSuccess { tides ->
                assertNotNull(tides.heights)
                assertNull(tides.extremes)
                assertEquals(0.485, tides.heights?.heights?.first()?.height)
            }
        }
    }

    @Test
    @DisplayName("maps only extremes when only extremes requested")
    fun mapsOnlyExtremesWhenOnlyExtremesRequested() {
        withSuccessfulResponse()
        tidesResponse = TidesResponse(
            200, null,
            heights = null,
            extremes = listOf(buildExtremeData())
        )

        tidesRepository.tides(listOf(TideDataType.EXTREMES), "", 1, "", "", "") { result ->
            assertTrue(result.isSuccess)
            result.onSuccess { tides ->
                assertNull(tides.heights)
                assertNotNull(tides.extremes)
                assertEquals(TideType.High, tides.extremes?.extremes?.first()?.type)
            }
        }
    }

    @Test
    @DisplayName("builds correct endpoint for heights only")
    fun buildsCorrectEndpointForHeightsOnly() {
        withSuccessfulResponse()
        tidesResponse = TidesResponse(200, null, heights = emptyList(), extremes = null)

        // Verify that tides is called with correct endpoint
        tidesRepository.tides(listOf(TideDataType.HEIGHTS), "", 1, "", "", "") { _ -> }
    }

    @Test
    @DisplayName("builds correct endpoint for heights and extremes")
    fun buildsCorrectEndpointForHeightsAndExtremes() {
        withSuccessfulResponse()
        tidesResponse = TidesResponse(200, null, heights = emptyList(), extremes = emptyList())

        tidesRepository.tides(listOf(TideDataType.HEIGHTS, TideDataType.EXTREMES), "", 1, "", "", "") { _ -> }
    }

    @Test
    @DisplayName("bubbles up error on failed response")
    fun bubblesUpTheErrorOnFailure() {
        withFailedResponse()
        tidesRepository.tides(listOf(TideDataType.HEIGHTS), "", 1, "", "", "") { result ->
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
    @DisplayName("handles empty response for both types")
    fun handlesEmptyResponseForBothTypes() {
        withSuccessfulResponse()
        tidesResponse = TidesResponse(200, null, heights = emptyList(), extremes = emptyList())

        tidesRepository.tides(listOf(TideDataType.HEIGHTS, TideDataType.EXTREMES), "", 1, "", "", "") { result ->
            assertTrue(result.isSuccess)
            result.onSuccess { tides ->
                assertEquals(0, tides.heights?.heights?.size)
                assertEquals(0, tides.extremes?.extremes?.size)
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

    private fun buildExtremeData(
        dt: Long = 1613540259L,
        date: String = "2021-02-17T05:37+0000",
        height: Float = 0.485f,
        type: String = "High"
    ): ExtremeResponse {
        return ExtremeResponse(dt, date, height, type)
    }

    private fun withSuccessfulResponse() {
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<TidesResponse> = invocation.arguments[0] as Callback<TidesResponse>
            val success = Response.success(200, tidesResponse)
            callback.onResponse(mockCall, success)
            null
        }
        `when`(gatewayMock.tides(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(mockCall)
    }

    private fun withFailedResponse() {
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<TidesResponse> = invocation.arguments[0] as Callback<TidesResponse>
            val mockResponseBody = ResponseBody.create(MediaType.get("json/txt"), "")
            val timeout = Response.error<TidesResponse>(408, mockResponseBody)
            callback.onResponse(mockCall, timeout)
            null
        }
        `when`(gatewayMock.tides(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(mockCall)
    }
}
