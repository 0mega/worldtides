# Data Model: Heights API

## Core Entities

### TideHeights
Wrapper for the list of tide heights.

| Field | Type | Description |
|-------|------|-------------|
| `heights` | `List<Height>` | Collection of predicted tide heights |

### Height
Represents a single tide height prediction at a specific time.

| Field | Type | Description |
|-------|------|-------------|
| `date` | `Date` (parsed from String) | The timestamp of the prediction |
| `height` | `Double` (or `Float`) | The height level (datum relative) |
| `dt` | `Long` | Unix timestamp (epoch) |

## Internal DTOs (Data Transfer Objects)

### TideHeightsResponse
Parsed directly from JSON API response.

```kotlin
data class TideHeightsResponse(
    val status: Int,
    val error: String?,
    val heights: List<HeightResponse>
)
```

### HeightResponse
Parsed directly from JSON item.

```kotlin
data class HeightResponse(
    val dt: Long,
    val date: String, // format: "yyyy-MM-ddTHH:mm+0000"
    val height: Double,
    // Note: 'type' is not expected for simple heights, or ignored if present
)
```

---

## Flexible Tides Data

### Tides
Represents a response containing any combination of requested tide data types.

| Field | Type | Description |
|-------|------|-------------|
| `heights` | `TideHeights?` | Tide heights (nullable if not requested) |
| `extremes` | `TideExtremes?` | Tide extremes (nullable if not requested) |
| `stations` | `TideStations?` | *(Future)* Station metadata |
| `datums` | `TideDatums?` | *(Future)* Datum information |

### TidesResponse (DTO)
Parsed from stacked API response (e.g., `?heights&extremes`).

```kotlin
data class TidesResponse(
    val status: Int,
    val error: String?,
    val heights: List<HeightResponse>?,
    val extremes: List<ExtremeResponse>?
    // Future: stations, datums
)
```

### TideDataType (Enum)
Specifies which data types to request from the API.

```kotlin
enum class TideDataType {
    HEIGHTS,
    EXTREMES
    // Future: STATIONS, DATUMS
}
```

---

## Generic Callback

### TidesCallback<T>
Generic callback interface to support multiple result types.

```kotlin
interface TidesCallback<T> {
    fun result(data: T)
    fun error(error: Error)
}
```

**Note**: This is a **breaking change** for existing consumers. Migration strategy: Deprecate existing `TidesCallback` and introduce `TidesCallback<T>` as the new standard.
