# Research: Support Heights API

**Status**: Complete
**Date**: 2026-01-09

## Decisions

### 1. API Endpoint
- **Decision**: Use `v2` endpoint with `heights` query parameter.
- **Rationale**: README confirms `Heights` is a `v2` API request. Existing `WorldTidesGateway` uses `@GET("v2?extremes")`.
- **Implementation**: `@GET("v2?heights")` in `WorldTidesGateway`.

### 2. Data Models
- **Decision**: Create `TideHeights` (wrapper) and `Height` (entity) models, mirroring `TideExtremes` and `Extreme`.
- **Rationale**: Consistency with existing codebase patterns.
- **Structure**:
    - `TideHeightsResponse` (Status, Error, HeightsList)
    - `HeightResponse` (dt, date, height) - Note: `type` field from `Extreme` is likely not present or not relevant for raw heights, unless it denotes prediction vs observation. We will assume prediction (default) for now.

### 3. Package Structure
- **Decision**: Move `WorldTidesRepository` and `WorldTidesGateway` to package root. Create new `heights` and `models` package hierarchies.
- **Rationale**: User request. Repository and Gateway are shared infrastructure, not specific to `extremes`.
- **Implementation**:
    - `com.oleksandrkruk.worldtides.WorldTidesRepository` (moved)
    - `com.oleksandrkruk.worldtides.WorldTidesGateway` (moved)
    - `com.oleksandrkruk.worldtides.heights.models.*`
    - `com.oleksandrkruk.worldtides.heights.data.*`
    - `com.oleksandrkruk.worldtides.models.Tides`
    - `com.oleksandrkruk.worldtides.models.TideDataType`

### 4. Flexible getTides Endpoint
- **Decision**: Support stacked API requests via dynamic query parameters based on `TideDataType` list.
- **Rationale**: User request. The WorldTides API allows stacking multiple data types (e.g., `?heights&extremes`).
- **Implementation**: `getTides(dataTypes: List<TideDataType>)` dynamically builds the endpoint query.

### 5. Generic TidesCallback
- **Decision**: Refactor `TidesCallback` to be generic (`TidesCallback<T>`).
- **Rationale**: User request to avoid proliferating type-specific callback interfaces.
- **Breaking Change**: Yes. Existing consumers using `TidesCallback` will need to update their code.

## Alternatives Considered

### Co-location in 'extremes' package
- **Idea**: Reuse existing `extremes` package.
- **Rejected**: User explicitly requested separation into `heights` package and shared `models` package.

### Suspend Functions
- **Idea**: Use Kotlin Coroutines (`suspend`) for the new method.
- **Rejected**: Constitution requires Java interoperability. Existing pattern uses `Callback`. We will stick to `Callback` (wrapped in `(Result) -> Unit`) to match `getTideExtremes`.

### Type-Specific Callbacks (e.g., TideHeightsCallback)
- **Idea**: Create a separate callback interface for each data type.
- **Rejected**: User explicitly requested a generic `TidesCallback<T>` to reduce code duplication.
