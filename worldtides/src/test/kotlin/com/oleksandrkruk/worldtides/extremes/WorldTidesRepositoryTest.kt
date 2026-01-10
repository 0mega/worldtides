package com.oleksandrkruk.worldtides.extremes

import com.oleksandrkruk.worldtides.WorldTidesGateway
import com.oleksandrkruk.worldtides.WorldTidesRepository
import com.oleksandrkruk.worldtides.extremes.data.ExtremeResponse
import com.oleksandrkruk.worldtides.extremes.data.TideExtremesResponse
import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import com.oleksandrkruk.worldtides.extremes.models.TideType
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
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
class WorldTidesRepositoryTest {

    @Mock
    private lateinit var gatewayMock: WorldTidesGateway
    @Mock
    lateinit var dateFormatterMock: SimpleDateFormat
    @Mock
    private lateinit var mockCall: Call<TideExtremesResponse>

    private lateinit var tidesResponse: TideExtremesResponse
    private lateinit var tidesRepository: WorldTidesRepository

    val today = Date()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        `when`(dateFormatterMock.parse(ArgumentMatchers.any())).thenReturn(today)
        tidesRepository = WorldTidesRepository(gatewayMock, dateFormatterMock)
    }

    @Test
    fun mapsCorrectlyFromDataToModel() {
        withSuccessfulResponse()

        tidesResponse = TideExtremesResponse(200, null, listOf(buildExtremeData()))

        tidesRepository.extremes("" ,1, "", "", "") { result: Result<TideExtremes> ->
            assertTrue(result.isSuccess)
            result.onSuccess { tides: TideExtremes ->
                assertEquals(1, tides.extremes.size)
                assertEquals(today.toString(), tides.extremes.first().date.toString())
                assertEquals(0.45f, tides.extremes.first().height)
                assertEquals(TideType.High, tides.extremes.first().type)
            }

            result.onFailure {
                fail("should never invoke failure on successful response")
            }
        }
    }

    @Test
    fun bubblesUpTheErrorOnFailure() {
        withFailedResponse()
        tidesRepository.extremes("" ,1, "", "", "") { result: Result<TideExtremes> ->
                assertTrue(result.isFailure)
                result.onSuccess { _: TideExtremes ->
                    fail("should never invoke success on failed response")
                }

                result.onFailure { error: Throwable ->
                    assertEquals(Error::class, error::class)
                }
        }
    }

    private fun buildExtremeData(
        dt: Long = 6453415263L,
        date: String = "2021-02-17T05:37+0000",
        height: Float = 0.45f,
        type: String = "High"
    ) : ExtremeResponse {
        return ExtremeResponse(dt, date, height, type)
    }

    private fun withSuccessfulResponse() {
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<TideExtremesResponse> = invocation.arguments[0] as Callback<TideExtremesResponse>
            val success = Response.success(200, tidesResponse)
            callback.onResponse(mockCall, success)
            null
        }
        `when`(gatewayMock.extremes(anyString(), anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(mockCall)
    }

    private fun withFailedResponse() {
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<TideExtremesResponse> = invocation.arguments[0] as Callback<TideExtremesResponse>
            val mockResponseBody = ResponseBody.create(MediaType.get("json/txt"), "")
            val timeout = Response.error<TideExtremesResponse>(408, mockResponseBody)
            callback.onResponse(mockCall, timeout)
            null
        }
        `when`(gatewayMock.extremes(anyString(), anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(mockCall)
    }
}
