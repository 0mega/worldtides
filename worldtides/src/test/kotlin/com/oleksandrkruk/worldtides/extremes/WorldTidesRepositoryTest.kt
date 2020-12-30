package com.oleksandrkruk.worldtides.extremes

import com.oleksandrkruk.worldtides.TidesCallback
import com.oleksandrkruk.worldtides.extremes.data.ExtremeResponse
import com.oleksandrkruk.worldtides.extremes.data.TideExtremesResponse
import com.oleksandrkruk.worldtides.extremes.models.TideExtremes
import com.oleksandrkruk.worldtides.extremes.models.TideType
import org.junit.Assert.assertEquals
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
import kotlin.test.assertTrue

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
        MockitoAnnotations.initMocks(this)

        `when`(dateFormatterMock.parse(ArgumentMatchers.any())).thenReturn(today)
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<TideExtremesResponse> = invocation.arguments[0] as Callback<TideExtremesResponse>
            val success = Response.success(200, tidesResponse)
            callback.onResponse(mockCall, success)
            null
        }
        `when`(gatewayMock.extremes(anyString(), anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(mockCall)

        tidesRepository = WorldTidesRepository(gatewayMock, dateFormatterMock)
    }

    @Test
    fun mapsCorrectlyFromDataToModel() {
        tidesResponse = TideExtremesResponse(200, null, listOf(buildExtremeData()))

        tidesRepository.extremes("" ,1, "", "", "", object : TidesCallback {

            override fun result(result: Result<TideExtremes>) {
                assertTrue(result.isSuccess)
                result.onSuccess { tides ->
                    assertEquals(1, tides.extremes.size)
                    assertEquals(today.toString(), tides.extremes.first().date.toString())
                    assertEquals(0.45f, tides.extremes.first().height)
                    assertEquals(TideType.High, tides.extremes.first().type)
                }
            }
        })
    }

    private fun buildExtremeData(
        dt: Long = 6453415263L,
        date: String = "2021-02-17T05:37+0000",
        height: Float = 0.45f,
        type: String = "High"
    ) : ExtremeResponse {
        return ExtremeResponse(dt, date, height, type)
    }
}
