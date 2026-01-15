# Implementation Plan: Support Heights API

**Branch**: `001-support-heights-api` | **Date**: 2026-01-09 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `specs/001-support-heights-api/spec.md`

## Summary

Implement `getTideHeights` and `getTides` in the WorldTides library to enable fetching tide height predictions and flexible combined tide data from the WorldTides v2 API. This involves:
1. Refactoring `TidesCallback` to be generic (`TidesCallback<T>`).
2. Creating new data models for Heights (`TideHeights`, `Height`) and flexible Tides (`Tides`, `TideDataType`).
3. Extending the existing Retrofit/Repository architecture with new endpoints.

> [!IMPORTANT]
> **Breaking Change**: Refactoring `TidesCallback` to a generic interface is a breaking change for existing consumers using the Java-style callback.

## Technical Context

**Language/Version**: Kotlin 1.9+ (Java 11+ compatible)
**Primary Dependencies**: Retrofit 2, Moshi (existing in project)
**Storage**: N/A (Stateless library)
**Testing**: JUnit 4/5, MockK (assumed based on standard Android/Kotlin libs)
**Target Platform**: Android / JVM
**Project Type**: Library
**Performance Goals**: Standard network latency; efficient parsing of potential large lists.
**Constraints**: Must maintain Java interoperability and strict API fidelity.
**Scale/Scope**: 2 new API methods (`getTideHeights`, `getTides`), ~6-8 new classes, 1 refactored interface, 1 enum.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **Kotlin-First, Java-Compatible**: Plan includes both Suspend (internal/future) or Callback (current pattern) interfaces. (Actually, current pattern assumes Callback for both Java/Kotlin via `Result`).
- [x] **Strict API Fidelity**: Naming will mirror API (`Heights`, `dt`, `height`).
- [x] **Type Safety**: Using `Result<T>` and strong types.
- [x] **Architecture Standards**: Using Retrofit/Moshi, separating Repository/Service.
- [x] **QA & Testing**: Plan includes unit testing.

## Project Structure

### Documentation (this feature)

```text
specs/001-support-heights-api/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
└── tasks.md             # Phase 2 output
```

### Source Code

```text
worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/
├── WorldTides.kt           # Main entry point (Modify: add getTideHeights, getTides)
├── WorldTidesRepository.kt # (MOVE from extremes + Modify: add heights, tides methods)
├── WorldTidesGateway.kt    # (MOVE from extremes + Modify: add heights, tides endpoints)
├── TidesCallback.kt        # (REFACTOR: Make generic TidesCallback<T>)
├── models/                 # [NEW] Shared models
│   ├── Tides.kt            # [NEW] Flexible result container
│   ├── TidesResponse.kt    # [NEW] API response DTO
│   └── TideDataType.kt     # [NEW] Enum for data types
├── extremes/               # Existing feature models
│   ├── data/
│   │   └── ...             # Existing DTOs
│   └── models/
│       └── ...             # Existing models (TideExtremes, Extreme)
├── heights/                # [NEW]
│   ├── data/
│   │   ├── TideHeightsResponse.kt # [NEW]
│   │   └── HeightResponse.kt      # [NEW]
│   └── models/
│       ├── TideHeights.kt         # [NEW]
│       └── Height.kt              # [NEW]
```

**Structure Decision**: Repository (`WorldTidesRepository`) and Gateway (`WorldTidesGateway`) moved to package root as shared infrastructure. Feature-specific models remain in their respective packages (`extremes/`, `heights/`). Shared models (`Tides`, `TideDataType`) in `models/` package.

## Complexity Tracking

N/A - Standard implementation.
