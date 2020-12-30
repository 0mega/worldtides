package com.oleksandrkruk.worldtides

import com.oleksandrkruk.worldtides.extremes.models.TideExtremes

interface TidesCallback {
    fun result(result: Result<TideExtremes>)
}
