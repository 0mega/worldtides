# Feature Specification: Support Heights API

**Feature Branch**: `001-support-heights-api`
**Created**: 2026-01-09
**Status**: Implemented

## User Scenarios

### User Story 1 - Retrieve Tide Heights (Priority: P1)

As a developer using the library, I want to fetch predicted tide heights for a specific location and date range so that I can use this data to display tide curves/charts in my application.

**Acceptance Scenarios**:

1. **Given** the library is initialized with a valid API key, **When** requesting tide heights for a valid location and time range, **Then** the result contains a list of tide height records.
2. **Given** an invalid API key, **When** requesting tide heights, **Then** an error indicating authentication failure is returned.
3. **Given** a location with no available data, **When** requesting tide heights, **Then** the library returns an empty result or appropriate specific error, not a generic crash.

### User Story 2 - Retrieve Tides with Flexible Data Types (Priority: P2)

As a developer, I want to fetch tide data by specifying which data types to include (e.g., heights, extremes) in a single API call so that I can reduce network overhead and get exactly the data I need.

**Acceptance Scenarios**:

1. **Given** the library is initialized with a valid API key, **When** requesting `getTides([HEIGHTS, EXTREMES])`, **Then** the result contains both heights and extremes data.
2. **Given** one data type is unavailable for a location, **When** requesting multiple data types, **Then** the available data is returned and the unavailable part is empty or null.
3. **Given** `getTides([HEIGHTS])` is called, **When** the request completes, **Then** only heights data is populated in the result.

### User Story 3 - Generic Callback Interface (Priority: P1)

As a library maintainer, I want the callback interface to be generic (`TidesCallback<T>`) so that it can be reused for different response types without code duplication.

**Acceptance Scenarios**:

1. **Given** a generic callback, **When** used with Heights request, **Then** it correctly receives `TideHeights`.
2. **Given** a generic callback, **When** used with Extremes request, **Then** it correctly receives `TideExtremes`.
3. **Given** a generic callback, **When** used with `getTides` request, **Then** it correctly receives `Tides`.
