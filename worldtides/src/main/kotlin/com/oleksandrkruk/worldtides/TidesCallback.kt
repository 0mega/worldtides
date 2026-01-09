package com.oleksandrkruk.worldtides

/**
 * Generic callback interface for WorldTides API responses.
 * Supports TideExtremes, TideHeights, Tides, and future types.
 *
 * @param T The result type.
 */
interface TidesCallback<T> {
    fun result(data: T)
    fun error(error: Error)
}
