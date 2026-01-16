# Implementation Plan: E2E Sample Client

**Branch**: `002-sample-client` | **Date**: 2026-01-16 | **Spec**: [spec.md](file:///Users/oleksandrkruk/projects/worldtides/specs/002-sample-client/spec.md)  
**Input**: Feature specification from `/specs/002-sample-client/spec.md`

## Summary

Create a standalone Gradle project (`sample-client/`) that exercises all WorldTides library API methods for end-to-end integration testing. The sample client will be run via manual GitHub Actions workflow dispatch, consuming the library from mavenLocal. Secrets are configured via `WORLD_TIDES_API_KEY` environment variable.

## Technical Context

**Language/Version**: Kotlin 1.9.20 (matches worldtides library)  
**Primary Dependencies**: worldtides library (via mavenLocal), JUnit 5, AssertJ  
**Storage**: N/A  
**Testing**: JUnit 5 with JUnit Platform  
**Target Platform**: JVM (CI runner - ubuntu-latest)  
**Project Type**: Standalone Gradle project (not submodule)  
**Performance Goals**: N/A (E2E tests, not performance testing)  
**Constraints**: Must consume library via mavenLocal; API key via environment variable  
**Scale/Scope**: Single test class with 6-7 test methods

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Notes |
|-----------|--------|-------|
| I. Kotlin-First, Java-Compatible | ✅ Pass | Sample client written in Kotlin; tests Kotlin callback API |
| II. Strict API Fidelity | ✅ Pass | Tests validate all current API methods |
| III. Type Safety & Null Safety | ✅ Pass | Tests validate response structures |
| Networking Layer | ✅ Pass | Not modifying networking; consuming as-is |
| Data Models | ✅ Pass | Tests validate immutable data class responses |
| SemVer | ✅ Pass | No versioning impact |
| Documentation | ✅ Pass | Sample client serves as usage documentation |
| QA & Testing | ✅ Pass | Adds E2E tests complementing unit tests |

**Result**: All gates pass. No violations to justify.

## Project Structure

### Documentation (this feature)

```text
specs/002-sample-client/
├── spec.md               # Feature specification
├── plan.md               # This file
├── research.md           # Phase 0 (N/A - no unknowns)
├── tasks.md              # Phase 2 output
└── checklists/
    └── requirements.md   # Spec quality checklist
```

### Source Code (repository root)

```text
sample-client/                    # STANDALONE project (not in settings.gradle.kts)
├── build.gradle.kts              # Declares dependency on worldtides via mavenLocal
├── settings.gradle.kts           # Self-contained Gradle settings
└── src/
    ├── main/kotlin/
    │   └── com/oleksandrkruk/worldtides/sample/
    │       └── SampleClient.kt   # Optional: runnable demo (main function)
    └── test/kotlin/
        └── com/oleksandrkruk/worldtides/sample/
            └── E2ETest.kt        # JUnit 5 E2E integration tests
```

### CI Workflow

```text
.github/workflows/
└── e2e.yml                       # NEW: Manual workflow_dispatch trigger
```

**Structure Decision**: Standalone Gradle project in `sample-client/` directory. Completely separate from the worldtides library build - building worldtides does NOT build sample-client. The sample-client consumes worldtides via mavenLocal after explicit publish.

## Implementation Files

### 1. sample-client/settings.gradle.kts

```kotlin
rootProject.name = "sample-client"
```

### 2. sample-client/build.gradle.kts

```kotlin
plugins {
    kotlin("jvm") version "1.9.20"
    application
}

repositories {
    mavenLocal()  // Consume worldtides from local publish
    mavenCentral()
}

dependencies {
    implementation("com.oleksandrkruk:worldtides:+")  // Latest from mavenLocal
    
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

application {
    mainClass.set("com.oleksandrkruk.worldtides.sample.SampleClientKt")
}

tasks.test {
    useJUnitPlatform()
    // Pass environment variable to tests
    environment("WORLD_TIDES_API_KEY", System.getenv("WORLD_TIDES_API_KEY") ?: "")
}

kotlin {
    jvmToolchain(8)
}
```

### 3. sample-client/src/test/kotlin/.../E2ETest.kt

JUnit 5 tests covering:
- `testGetTideExtremes()` - FR-001
- `testGetTideHeights()` - FR-002
- `testGetTidesHeightsOnly()` - FR-003
- `testGetTidesExtremesOnly()` - FR-004
- `testGetTidesBothTypes()` - FR-005
- `testInvalidApiKeyReturnsError()` - FR-006

Each test uses `CountDownLatch` for async callback synchronization and validates response structure.

### 4. .github/workflows/e2e.yml

```yaml
name: E2E Tests

on:
  workflow_dispatch:  # Manual trigger only (FR-009)

jobs:
  e2e:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'jetbrains'
          java-version: '21'
      
      # FR-010: Publish worldtides to mavenLocal first
      - name: Publish worldtides to mavenLocal
        run: ./gradlew :worldtides:publishToMavenLocal
      
      # Run sample-client tests with API key
      - name: Run E2E Tests
        env:
          WORLD_TIDES_API_KEY: ${{ secrets.WORLD_TIDES_API_KEY }}
        run: |
          cd sample-client
          ./gradlew test
```

## Complexity Tracking

No constitution violations. Table not needed.

## Phase 0: Research

**No unknowns detected.** All technical context is resolved:
- Language/framework matches existing library
- mavenLocal consumption is standard Gradle practice
- JUnit 5 test structure mirrors existing tests

**Output**: research.md not required (no NEEDS CLARIFICATION items).

## Phase 1: Design & Contracts

**Data Model**: N/A - Sample client consumes library models as-is, does not define new entities.

**API Contracts**: N/A - Sample client is a consumer, not a producer of APIs.

**Output**: data-model.md and contracts/ not required for this feature.

## Next Steps

Run `/speckit.tasks` to break this plan into executable tasks.
