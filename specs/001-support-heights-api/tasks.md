# Tasks: Support Heights API

**Input**: Design documents from `/specs/001-support-heights-api/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Organization**: Tasks are grouped by user story. Each task produces a compilable, testable, committable increment.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Move shared infrastructure to package root level.

- [x] T001 Move `WorldTidesRepository.kt` from `extremes/` to package root `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTidesRepository.kt`
- [x] T002 Move `WorldTidesGateway.kt` from `extremes/` to package root `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTidesGateway.kt`
- [x] T003 Update imports in `WorldTides.kt` to reference new package locations
- [x] T004 Verify project compiles and existing tests pass after refactoring

**Checkpoint**: Shared infrastructure relocated. Codebase compiles and tests pass.

---

## Phase 2: Foundational (Generic Callback - US3)

**Purpose**: Refactor `TidesCallback` to generic interface. This is a **prerequisite** for US1 and US2.

**Goal**: Enable type-safe callbacks for any response type.

**Independent Test**: Existing `getTideExtremes` works with `TidesCallback<TideExtremes>`.

> [!WARNING]
> **Breaking Change**: This refactors the existing `TidesCallback` interface.

- [x] T005 Refactor `TidesCallback.kt` to `TidesCallback<T>` in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/TidesCallback.kt`
- [x] T006 Update `WorldTides.getTideExtremes` Java overload to use `TidesCallback<TideExtremes>` in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTides.kt`
- [x] T007 Update `WorldTidesRepository.extremes` to work with generic callback in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTidesRepository.kt`
- [x] T008 Verify project compiles and existing `getTideExtremes` tests pass

**Checkpoint**: Generic callback ready. Existing functionality unchanged. Codebase compiles and tests pass.

---

## Phase 3: User Story 1 - Retrieve Tide Heights (Priority: P1) 🎯 MVP

**Goal**: Enable developers to fetch tide heights for a location and date range.

**Independent Test**: Call `getTideHeights()` and receive a list of height objects with timestamps.

### Implementation for User Story 1

#### Models

- [x] T009 [P] [US1] Create `Height.kt` data class in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/heights/models/Height.kt`
- [x] T010 [P] [US1] Create `TideHeights.kt` wrapper in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/heights/models/TideHeights.kt`

#### DTOs

- [x] T011 [P] [US1] Create `HeightResponse.kt` DTO in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/heights/data/HeightResponse.kt`
- [x] T012 [P] [US1] Create `TideHeightsResponse.kt` DTO in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/heights/data/TideHeightsResponse.kt`

#### Gateway & Repository

- [x] T013 [US1] Add `heights` endpoint to `WorldTidesGateway.kt` in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTidesGateway.kt`
- [x] T014 [US1] Add `heights` method to `WorldTidesRepository.kt` in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTidesRepository.kt`

#### Public API

- [x] T015 [US1] Add `getTideHeights` method (Kotlin lambda) to `WorldTides.kt` in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTides.kt`
- [x] T016 [US1] Add `getTideHeights` method (Java callback) to `WorldTides.kt` in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTides.kt`

#### Documentation

- [x] T017 [US1] Update README.md with Heights API usage examples in `README.md`
- [x] T018 [US1] Verify project compiles and `getTideHeights` can be called

**Checkpoint**: User Story 1 complete. Developers can fetch tide heights. Codebase compiles and tests pass.

---

## Phase 4: User Story 2 - Retrieve Tides with Flexible Data Types (Priority: P2)

**Goal**: Enable developers to fetch multiple data types (heights, extremes) in a single API call.

**Independent Test**: Call `getTides([HEIGHTS, EXTREMES])` and receive a response with both data sets.

### Implementation for User Story 2

#### Shared Models

- [x] T019 [P] [US2] Create `TideDataType.kt` enum in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/models/TideDataType.kt`
- [x] T020 [P] [US2] Create `Tides.kt` wrapper in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/models/Tides.kt`
- [x] T021 [P] [US2] Create `TidesResponse.kt` DTO in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/models/TidesResponse.kt`

#### Gateway & Repository

- [x] T022 [US2] Add dynamic `tides` endpoint to `WorldTidesGateway.kt` that accepts query parameters in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTidesGateway.kt`
- [x] T023 [US2] Add `tides` method to `WorldTidesRepository.kt` that builds query from `TideDataType` list in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTidesRepository.kt`

#### Public API

- [x] T024 [US2] Add `getTides` method (Kotlin lambda) to `WorldTides.kt` in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTides.kt`
- [x] T025 [US2] Add `getTides` method (Java callback) to `WorldTides.kt` in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTides.kt`

#### Documentation

- [x] T026 [US2] Update README.md with `getTides` usage examples in `README.md`
- [x] T027 [US2] Verify project compiles and `getTides` can be called with multiple data types

**Checkpoint**: User Story 2 complete. Developers can fetch flexible tide data. Codebase compiles and tests pass.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Final cleanup and documentation.

- [ ] T028 Update README.md supported API table (mark Heights as Yes) in `README.md`
- [ ] T029 Add KDoc/Javadoc to all new public methods and classes
- [ ] T030 Run full test suite and verify all tests pass
- [ ] T031 Verify quickstart.md examples compile and work

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: No dependencies - start immediately
- **Phase 2 (Foundational/US3)**: Depends on Phase 1 - BLOCKS US1 and US2
- **Phase 3 (US1)**: Depends on Phase 2
- **Phase 4 (US2)**: Depends on Phase 2 (can run in parallel with US1 if desired)
- **Phase 5 (Polish)**: Depends on US1 and US2 completion

### User Story Dependencies

- **US3 (Generic Callback)**: Foundational - must complete first
- **US1 (Heights)**: Depends on US3 only - can run in parallel with US2
- **US2 (Flexible Tides)**: Depends on US3 only - can run in parallel with US1

### Within Each User Story

- Models before DTOs (or parallel if independent files)
- DTOs before Gateway/Repository
- Gateway/Repository before Public API
- Public API before Documentation

### Parallel Opportunities

```text
# Models (T009, T010) can run in parallel
# DTOs (T011, T012) can run in parallel
# Shared models (T019, T020, T021) can run in parallel
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (T001-T004)
2. Complete Phase 2: Generic Callback (T005-T008)
3. Complete Phase 3: User Story 1 (T009-T018)
4. **STOP and VALIDATE**: Test `getTideHeights` independently
5. Commit and merge if ready

### Full Feature Delivery

1. Complete MVP (above)
2. Add Phase 4: User Story 2 (T019-T027)
3. Complete Phase 5: Polish (T028-T031)
4. Final validation and merge

---

## Notes

- Each task produces a **compilable codebase**
- Commit after each task or logical group
- [P] tasks can run in parallel (different files)
- Verify tests pass after each phase
- Breaking change in Phase 2 requires version bump
