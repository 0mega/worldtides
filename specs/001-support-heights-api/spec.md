# Feature Specification: Support Heights API

**Feature Branch**: `001-support-heights-api`
**Created**: 2026-01-09
**Status**: Draft
**Input**: User description: "based on the supported API calls documented in the readme create a specification to extend the library to support the Hights API request"

## User Scenarios & Testing

### User Story 1 - Retrieve Tide Heights (Priority: P1)

As a developer using the library, I want to fetch predicted tide heights for a specific location and date range so that I can use this data to display tide curves/charts in my application.

**Why this priority**: "Heights" is a fundamental dataset provided by the API that enables detailed visualization, which is currently missing from the library.

**Independent Test**:
A developer can write a script or test case that invokes the new "Heights" method with valid credentials and receives a collection of height objects with timestamps and values.

**Acceptance Scenarios**:

1. **Given** the library is initialized with a valid API key, **When** requesting tide heights for a valid location and time range, **Then** the result contains a list of tide height records.
2. **Given** an invalid API key, **When** requesting tide heights, **Then** an error indicating authentication failure is returned.
3. **Given** a location with no available data, **When** requesting tide heights, **Then** the library returns an empty result or appropriate specific error, not a generic crash.

---

### User Story 2 - Retrieve Tides with Flexible Data Types (Priority: P2)

As a developer, I want to fetch tide data by specifying which data types to include (e.g., heights, extremes) in a single API call so that I can reduce network overhead and get exactly the data I need.

**Why this priority**: The WorldTides API supports stacking multiple data types (e.g., `?heights&extremes`). Enabling this in the library provides efficiency and future extensibility (stations, datums, etc.).

**Independent Test**:
A developer can invoke `getTides` with a list of data types and receive a response containing the requested data.

**Acceptance Scenarios**:

1. **Given** the library is initialized with a valid API key, **When** requesting `getTides([HEIGHTS, EXTREMES])`, **Then** the result contains both heights and extremes data.
2. **Given** one data type is unavailable for a location, **When** requesting multiple data types, **Then** the available data is returned and the unavailable part is empty or null.
3. **Given** `getTides([HEIGHTS])` is called, **When** the request completes, **Then** only heights data is populated in the result.

---

### User Story 3 - Generic Callback Interface (Priority: P1)

As a library maintainer, I want the callback interface to be generic (`TidesCallback<T>`) so that it can be reused for different response types without code duplication.

**Why this priority**: Enables clean architecture and avoids proliferating type-specific callback interfaces.

**Independent Test**:
Unit tests verify that `TidesCallback<TideHeights>`, `TidesCallback<TideExtremes>`, and `TidesCallback<TideCombined>` all compile and function correctly.

**Acceptance Scenarios**:

1. **Given** a generic callback, **When** used with Heights request, **Then** it correctly receives `TideHeights`.
2. **Given** a generic callback, **When** used with Extremes request, **Then** it correctly receives `TideExtremes`.
3. **Given** a generic callback, **When** used with `getTides` request, **Then** it correctly receives `Tides`.

### Edge Cases

- **Invalid Parameters**: Requesting negative duration or invalid date formats.
- **Network Issues**: Connection timeout or loss during request.
- **API Changes**: Unexpected response format from the server.
- **Partial Responses (Combined)**: API returns one data type but not the other.

## Requirements

### Functional Requirements

- **FR-001**: The library MUST provide a method to request "Heights" data from the remote API.
- **FR-002**: The request method MUST accept parameters for Location (Latitude, Longitude), Start Date, and Duration (Days).
- **FR-003**: The library MUST parse the API response into a strongly-typed data structure representing Tide Heights (Time and Height).
- **FR-004**: The library MUST provide error handling mechanisms to report failures (Network, Auth, Validation) to the caller.
- **FR-005**: The feature MUST be fully interoperable with both Kotlin and Java applications.
- **FR-006**: The usage pattern (method signature, callback style) MUST remain consistent with existing library features (e.g. `getTideExtremes`).
- **FR-007**: The library MUST refactor `TidesCallback` to be generic (`TidesCallback<T>`) for type-safe result handling.
- **FR-008**: The library MUST support stacked API requests via a flexible `getTides` method that accepts a list of data types.
- **FR-009**: The library MUST provide a `Tides` data model that can hold any combination of requested data (Heights, Extremes, and future types like Stations, Datums).
- **FR-010**: The library MUST provide a `TideDataType` enum to specify which data types to request.

### Key Entities

- **TideHeights/TideHeight**: Data structure representing the height of the tide at a specific point in time.
- **Tides**: Data structure containing optional Heights, Extremes, and future data type lists.
- **TideDataType**: Enum representing the types of tide data that can be requested (HEIGHTS, EXTREMES, future: STATIONS, DATUMS).

## Success Criteria

### Measurable Outcomes

- **SC-001**: Developers can successfully retrieve and parse tide heights for a standard 7-day request.
- **SC-002**: The API surface for "Heights" matches the conventions of "Extremes" (consistency).
- **SC-003**: Test coverage extends to both Java and Kotlin consumers for this feature.
