# Feature Specification: E2E Sample Client

**Feature Branch**: `002-sample-client`
**Created**: 2026-01-16
**Status**: Implemented

## User Scenarios

### User Story 1 - Verify All Library API Methods Work (Priority: P1)

A CI pipeline operator wants to verify that the WorldTides library correctly integrates with the live World Tides API. They manually trigger an E2E workflow that runs the sample client against all supported API methods using a real API key.

**Acceptance Scenarios**:

1. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTideExtremes()`, **Then** the response contains a non-empty list of tide extremes with valid dates, heights, and tide types (High/Low).
2. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTideHeights()`, **Then** the response contains a non-empty list of tide heights with valid dates and height values.
3. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTides()` with HEIGHTS only, **Then** the response contains tide heights and extremes is null.
4. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTides()` with EXTREMES only, **Then** the response contains tide extremes and heights is null.
5. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTides()` with both HEIGHTS and EXTREMES, **Then** the response contains both heights and extremes data.

### User Story 2 - Error Handling Verification (Priority: P2)

A developer wants to verify that the library properly returns errors when given invalid input or credentials. The sample client should demonstrate proper error handling behavior.

**Acceptance Scenarios**:

1. **Given** an invalid API key, **When** any API method is called, **Then** the callback receives an error result (not a success with empty data).

### User Story 3 - CI Integration (Priority: P3)

A release engineer wants a GitHub Actions workflow that can be manually triggered to run E2E tests. The workflow should only execute on demand to control API usage costs.

**Acceptance Scenarios**:

1. **Given** the `e2e.yml` workflow exists, **When** a maintainer clicks "Run workflow" from GitHub Actions, **Then** the workflow runs the sample client tests with the configured API key secret.
2. **Given** the E2E workflow, **When** a push is made to any branch, **Then** the E2E workflow does NOT automatically trigger.
