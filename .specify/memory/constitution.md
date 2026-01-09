# WorldTides Constitution

## Core Principles

### I. Kotlin-First, Java-Compatible
This library is developed in Kotlin to leverage modern language features, safety, and conciseness. However, **Java interoperability is a first-class citizen**.
- All public APIs must be consumable from Java without awkward syntax (e.g., use `@JvmOverloads`, `@JvmStatic` where appropriate).
- Asynchronous operations must provide both Suspend functions (for Kotlin) and Callback interfaces (for Java).
- Avoid exposing Kotlin-specific types (like `inline` classes) in the public API if they degrade the Java experience.

### II. Strict API Fidelity
The library serves as a strongly-typed proxy for the [WorldTides API](https://www.worldtides.info/apidocs).
- **Naming**: Library models and methods should mirror the API's terminology unless it conflicts with standard Java/Kotlin naming conventions.
- **Completeness**: Aim to support all parameters and response fields available in the API version we target.
- **Updates**: Changes in the WorldTides API (v2, v3, etc.) should be reflected in the library with appropriate versioning.

### III. Type Safety & Null Safety
Users of this library should never have to parse raw JSON.
- All API responses must be mapped to strongly-typed data classes.
- Nullability must be strictly modeled: if a field can be missing in the API response, it must be nullable in the model.
- `Result<T>` or similar wrappers should be used to handle success/failure states explicitly.

## Architecture Standards

### Networking Layer
- **Retrofit & Moshi**: The library uses Retrofit for networking and Moshi for JSON parsing.
- **Encapsulation**: The internal networking stack (OkHttp client) is encapsulated to provide a simple "one-line" initialization experience.
- *Future Consideration*: Allow dependency injection for `OkHttpClient` to enable advanced configuration (caching, interceptors) by the consumer.

### Data Models
- **Immutability**: All data models (`TideExtremes`, `Extreme`, etc.) are immutable `data class` structures.
- **Serialization**: Use annotations (e.g., `@Json`) to map API JSON keys to clean Kotlin property names.

## Development Workflow

### Versioning
- **Semantic Versioning**: Follow [SemVer](https://semver.org/) (MAJOR.MINOR.PATCH).
  - MAJOR: Breaking API changes.
  - MINOR: New features (e.g., new API endpoints supported) in a backward-compatible manner.
  - PATCH: Bug fixes.

### Documentation
- **KDoc & Javadoc**: All public classes and methods must be documented.
- **Samples**: Code samples in README must include both Kotlin and Java examples side-by-side.

## Governance

### QA & Testing
- **Mandatory Testing**: 100% of the codebase must be covered by tests.
- **Unit Tests**: Required for all logic, data transformations, and utility functions.
- **Integration Tests**: Verification of API mapping and inter-component communication is critical.
- **Public API Review**: Any change to the public API requires a review focusing on "How does this look in Java?" and "How does this look in Kotlin?".

**Version**: 1.0.0 | **Ratified**: 2026-01-09
