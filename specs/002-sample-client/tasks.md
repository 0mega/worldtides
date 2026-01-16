# Tasks: E2E Sample Client

**Input**: Design documents from `/specs/002-sample-client/`  
**Prerequisites**: plan.md ✓, spec.md ✓  
**Approach**: TDD - write tests FIRST, ensure they FAIL, then implement

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Project Initialization)

**Purpose**: Create standalone sample-client project structure

- [ ] T001 Create `sample-client/` directory at repository root
- [ ] T002 Create `sample-client/settings.gradle.kts` with project name "sample-client"
- [ ] T003 Create `sample-client/build.gradle.kts` with Kotlin JVM, mavenLocal, JUnit 5, and AssertJ dependencies
- [ ] T004 Create directory structure: `sample-client/src/test/kotlin/com/oleksandrkruk/worldtides/sample/`
- [ ] T005 Verify project compiles: `cd sample-client && ./gradlew build` (should succeed with empty test class)

**Checkpoint**: Standalone project structure ready. Can now write tests.

---

## Phase 2: User Story 1 - Verify All Library API Methods Work (Priority: P1) 🎯 MVP

**Goal**: E2E tests validate all 5 API method variations return correct data

**Independent Test**: Run `WORLD_TIDES_API_KEY=<key> ./gradlew :sample-client:test` - all tests pass with valid API key

### Tests for User Story 1 (TDD - Write FIRST, verify FAIL)

> **TDD**: Write failing tests that define expected behavior, then implement code to make them pass

- [ ] T006 [US1] Create test file `sample-client/src/test/kotlin/com/oleksandrkruk/worldtides/sample/E2ETest.kt` with test class skeleton and API key from env var
- [ ] T007 [P] [US1] Write failing test `testGetTideExtremes()` - asserts response has non-empty extremes list with valid dates, heights, and TideType (FR-001)
- [ ] T008 [P] [US1] Write failing test `testGetTideHeights()` - asserts response has non-empty heights list with valid dates and height values (FR-002)
- [ ] T009 [P] [US1] Write failing test `testGetTidesHeightsOnly()` - asserts heights present and extremes null (FR-003)
- [ ] T010 [P] [US1] Write failing test `testGetTidesExtremesOnly()` - asserts extremes present and heights null (FR-004)
- [ ] T011 [P] [US1] Write failing test `testGetTidesBothTypes()` - asserts both heights and extremes present (FR-005)

### Implementation for User Story 1

- [ ] T012 [US1] Publish worldtides library: `./gradlew :worldtides:publishToMavenLocal`
- [ ] T013 [US1] Verify all US1 tests pass with valid API key: `WORLD_TIDES_API_KEY=<key> ./gradlew :sample-client:test`

**Checkpoint**: All 5 happy-path API tests pass. User Story 1 complete.

---

## Phase 3: User Story 2 - Error Handling Verification (Priority: P2)

**Goal**: E2E test verifies error callback is invoked with invalid API key

**Independent Test**: Run test with invalid API key - test passes (error correctly returned)

### Tests for User Story 2 (TDD - Write FIRST, verify FAIL)

- [ ] T014 [US2] Write failing test `testInvalidApiKeyReturnsError()` in `sample-client/src/test/kotlin/com/oleksandrkruk/worldtides/sample/E2ETest.kt` - asserts error callback invoked, not success (FR-006)

### Implementation for User Story 2

- [ ] T015 [US2] Verify US2 test passes: error test uses hardcoded invalid key and confirms error result

**Checkpoint**: Error handling verified. User Story 2 complete.

---

## Phase 4: User Story 3 - CI Integration (Priority: P3)

**Goal**: GitHub Actions workflow runs E2E tests on manual trigger only

**Independent Test**: Manually trigger workflow from GitHub Actions UI - tests run successfully

### Implementation for User Story 3 (No TDD - infrastructure config)

- [ ] T016 [US3] Create `.github/workflows/e2e.yml` with `workflow_dispatch` trigger only (FR-009)
- [ ] T017 [US3] Add step to publish worldtides to mavenLocal before running tests (FR-010)
- [ ] T018 [US3] Add step to run sample-client tests with `WORLD_TIDES_API_KEY` secret
- [ ] T019 [US3] Validate workflow YAML syntax: `yamllint .github/workflows/e2e.yml` or manual review

**Checkpoint**: CI workflow ready. User Story 3 complete.

---

## Phase 5: Polish & Verification

**Purpose**: Final validation and documentation

- [ ] T020 Run full test suite locally with valid API key to confirm all tests pass
- [ ] T021 [P] Update specs/002-sample-client/ docs to mark feature as implemented
- [ ] T022 Commit all changes with descriptive message

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - start immediately
- **US1 (Phase 2)**: Depends on Setup - write tests first, then verify pass
- **US2 (Phase 3)**: Depends on Setup - can run in parallel with US1
- **US3 (Phase 4)**: Depends on Setup - can run in parallel with US1/US2
- **Polish (Phase 5)**: Depends on all user stories complete

### Parallel Opportunities

```text
After Phase 1 (Setup):
┌─────────────────────────────────────────┐
│ Phase 2: US1 (API Tests)                │
├─────────────────────────────────────────┤
│ T007, T008, T009, T010, T011 [P]        │  ← Write all 5 tests in parallel
└─────────────────────────────────────────┘

After Phase 1 (Setup):
┌─────────────────────────────────────────┐
│ Phase 3: US2 (Error Test)               │  ← Can start in parallel with US1
└─────────────────────────────────────────┘

After Phase 1 (Setup):
┌─────────────────────────────────────────┐
│ Phase 4: US3 (CI Workflow)              │  ← Can start in parallel with US1/US2
└─────────────────────────────────────────┘
```

---

## Implementation Strategy

### TDD MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Write all US1 tests (T007-T011) - verify they compile but would fail without library
3. Publish library to mavenLocal (T012)
4. Run tests (T013) - verify all pass
5. **STOP and VALIDATE**: MVP complete, core E2E coverage achieved

### Incremental Delivery

1. Setup → US1 (API tests) → MVP! ✓
2. Add US2 (error handling) → Enhanced coverage
3. Add US3 (CI workflow) → Full automation

---

## Summary

| Phase | User Story | Task Count | Parallelizable |
|-------|------------|------------|----------------|
| 1 | Setup | 5 | - |
| 2 | US1 - API Tests | 8 | 5 tests [P] |
| 3 | US2 - Error Handling | 2 | - |
| 4 | US3 - CI Workflow | 4 | - |
| 5 | Polish | 3 | 1 [P] |
| **Total** | | **22** | **6** |
