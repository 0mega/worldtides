package com.oleksandrkruk.worldtides.extremes.data

import com.oleksandrkruk.worldtides.extremes.models.TideType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

class ExtremeResponseMappingTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.US)

    @Test
    @DisplayName("ExtremeResponse.toExtreme() maps all fields correctly")
    fun toExtremeMapsAllFieldsCorrectly() {
        val response = ExtremeResponse(
            dt = 1613540259L,
            date = "2021-02-17T05:37+0000",
            height = 1.5f,
            type = "High"
        )
        
        val extreme = response.toExtreme(dateFormat)
        
        assertEquals(dateFormat.parse("2021-02-17T05:37+0000"), extreme.date)
        assertEquals(1.5f, extreme.height)
        assertEquals(TideType.High, extreme.type)
    }

    @Test
    @DisplayName("ExtremeResponse.toExtreme() maps Low tide type")
    fun toExtremeMapsLowTideType() {
        val response = ExtremeResponse(
            dt = 1613540259L,
            date = "2021-02-17T11:45+0000",
            height = 0.3f,
            type = "Low"
        )
        
        val extreme = response.toExtreme(dateFormat)
        
        assertEquals(TideType.Low, extreme.type)
    }

    @Test
    @DisplayName("TideExtremesResponse.toTideExtremes() maps list correctly")
    fun toTideExtremesMapsListCorrectly() {
        val response = TideExtremesResponse(
            status = 200,
            error = null,
            extremes = listOf(
                ExtremeResponse(1L, "2021-02-17T05:37+0000", 1.5f, "High"),
                ExtremeResponse(2L, "2021-02-17T11:45+0000", 0.3f, "Low")
            )
        )
        
        val tideExtremes = response.toTideExtremes(dateFormat)
        
        assertEquals(2, tideExtremes.extremes.size)
        assertEquals(TideType.High, tideExtremes.extremes[0].type)
        assertEquals(TideType.Low, tideExtremes.extremes[1].type)
    }

    @Test
    @DisplayName("TideExtremesResponse.toTideExtremes() handles empty list")
    fun toTideExtremesHandlesEmptyList() {
        val response = TideExtremesResponse(
            status = 200,
            error = null,
            extremes = emptyList()
        )
        
        val tideExtremes = response.toTideExtremes(dateFormat)
        
        assertEquals(0, tideExtremes.extremes.size)
    }
}
