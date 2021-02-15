package com.oleksandrkruk.worldtides;

import com.oleksandrkruk.worldtides.extremes.models.TideExtremes;
import org.junit.jupiter.api.Test;
import java.util.*

class WorldTidesJavaApiTest {

    @Test
    fun providesJavaCompatibleApiForExtremes() {
        val wt = WorldTides.Builder().build("bar")
        wt.getTideExtremes(Date(), 5, "lat", "lon", object : TidesCallback {
            override fun result(tides: TideExtremes) {}
            override fun error(error: Error) {}
        })
    }
}
