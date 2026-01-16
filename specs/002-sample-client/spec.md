# Feature Specification: E2E Sample Client

**Feature Branch**: `002-sample-client`  
**Created**: 2026-01-16  
**Status**: Draft  
**Input**: User description: "Create a sample client for the WorldTides library that exercises all API use cases for end-to-end testing in CI"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Verify All Library API Methods Work (Priority: P1)

A CI pipeline operator wants to verify that the WorldTides library correctly integrates with the live World Tides API. They manually trigger an E2E workflow that runs the sample client against all supported API methods using a real API key.

**Why this priority**: Core purpose of the sample client — validates the library works end-to-end against the production API before releases.

**Independent Test**: Can be fully tested by running `./gradlew :sample-client:test` with a valid API key and observing that all API methods return valid tide data.

**Acceptance Scenarios**:

1. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTideExtremes()`, **Then** the response contains a non-empty list of tide extremes with valid dates, heights, and tide types (High/Low).

2. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTideHeights()`, **Then** the response contains a non-empty list of tide heights with valid dates and height values.

3. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTides()` with HEIGHTS only, **Then** the response contains tide heights and extremes is null.

4. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTides()` with EXTREMES only, **Then** the response contains tide extremes and heights is null.

5. **Given** a valid World Tides API key is configured, **When** the E2E tests run for `getTides()` with both HEIGHTS and EXTREMES, **Then** the response contains both heights and extremes data.

---

### User Story 2 - Error Handling Verification (Priority: P2)

A developer wants to verify that the library properly returns errors when given invalid input or credentials. The sample client should demonstrate proper error handling behavior.

**Why this priority**: Error handling is important but secondary to happy-path functionality.

**Independent Test**: Can be tested by running the E2E test with an invalid API key and verifying that an error is returned (not a crash).

**Acceptance Scenarios**:

1. **Given** an invalid API key, **When** any API method is called, **Then** the callback receives an error result (not a success with empty data).

---

### User Story 3 - CI Integration (Priority: P3)

A release engineer wants a GitHub Actions workflow that can be manually triggered to run E2E tests. The workflow should only execute on demand to control API usage costs.

**Why this priority**: Enables continuous integration but not blocking for library functionality.

**Independent Test**: Can be tested by manually triggering the workflow from GitHub Actions UI and verifying it completes successfully.

**Acceptance Scenarios**:

1. **Given** the `e2e.yml` workflow exists, **When** a maintainer clicks "Run workflow" from GitHub Actions, **Then** the workflow runs the sample client tests with the configured API key secret.

2. **Given** the E2E workflow, **When** a push is made to any branch, **Then** the E2E workflow does NOT automatically trigger.

---

### Edge Cases

- What happens when the API key is missing (empty environment variable)?
- What happens when network connectivity fails during an API call?
- What happens when requesting tide data for an invalid location (middle of land)?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Sample client MUST call `getTideExtremes()` with valid parameters and validate the response structure
- **FR-002**: Sample client MUST call `getTideHeights()` with valid parameters and validate the response structure
- **FR-003**: Sample client MUST call `getTides()` with single data type (HEIGHTS only) and verify only heights are returned
- **FR-004**: Sample client MUST call `getTides()` with single data type (EXTREMES only) and verify only extremes are returned
- **FR-005**: Sample client MUST call `getTides()` with multiple data types (HEIGHTS + EXTREMES) and verify both are returned
- **FR-006**: Sample client MUST verify error handling when an invalid API key is used
- **FR-007**: Sample client MUST read the API key from the `WORLD_TIDES_API_KEY` environment variable
- **FR-008**: Sample client MUST be a standalone Gradle project (not a submodule of worldtides)
- **FR-009**: E2E workflow MUST only trigger on manual `workflow_dispatch`
- **FR-010**: E2E workflow MUST publish worldtides to mavenLocal before running sample client tests

### Key Entities

- **WorldTides Client**: The main library under test, initialized with an API key via `WorldTides.Builder().build(apiKey)`
- **TideExtremes**: Contains list of `Extreme` objects (date, height, type)
- **TideHeights**: Contains list of `Height` objects (date, height)
- **Tides**: Combined response containing optional heights and extremes
- **TideDataType**: Enum specifying which data types to request (HEIGHTS, EXTREMES)

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: All 5 API method variations are exercised by the sample client tests
- **SC-002**: Sample client tests pass when given a valid API key (verified by manual E2E workflow run)
- **SC-003**: Sample client test for error handling correctly identifies an invalid API key scenario
- **SC-004**: E2E workflow can be triggered manually from GitHub Actions UI
- **SC-005**: E2E workflow does not run automatically on push or pull request events
