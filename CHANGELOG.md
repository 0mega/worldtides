# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.1] - 2026-02-06

### Added
- **Custom OkHttpClient Support**: `WorldTides.Builder` now exposes a `client(OkHttpClient)` method, allowing users to provide their own configured client.
- **Logging Configuration**: Added `loggingLevel(HttpLoggingInterceptor.Level)` to `WorldTides.Builder` for easier debugging.

### Changed
- Exposed `okhttp3:okhttp` and `okhttp3:logging-interceptor` as API dependencies to allow better consumer configuration.

## [2.0.0] - 2026-01-15

### ⚠️ BREAKING CHANGES

- **`TidesCallback` is now generic**: The callback interface has been refactored to `TidesCallback<T>` to support multiple response types.

  **Migration for Java users:**
  ```java
  // Before
  new TidesCallback() {
      @Override public void result(TideExtremes tides) { }
      @Override public void error(Error error) { }
  }
  
  // After
  new TidesCallback<TideExtremes>() {
      @Override public void result(TideExtremes data) { }
      @Override public void error(Error error) { }
  }
  ```

- **`Height.date` is now non-nullable**: Changed from `Date?` to `Date` for consistency with `Extreme.date`.

### Added

- `getTideHeights()` - Fetch predicted tide heights for a location
- `getTides()` - Flexible method to fetch any combination of tide data types
- `TideDataType` enum for specifying data types (HEIGHTS, EXTREMES)
- `Tides` model for combined tide data responses
- `TideHeights` and `Height` models for height data
- Comprehensive test coverage for all new functionality

### Changed

- Refactored `WorldTidesRepository` and `WorldTidesGateway` to package root for shared usage
- Internal: Added DTO mapping extension functions for cleaner code
- Internal: Extracted `enqueueMapped()` extension for callback boilerplate

### Internal

- Bump Kotlin version to 1.9.20
- Bump Gradle to 8.9
- Bump mockito to 5.13.0
- Bump to JDK_1_8
- Replace jcenter() with mavenCentral()
- Update github actions setup
- Added SpekKit and project constitution

## 1.0.0 - 2021-02-15

### Added

- Compatibility with Java

### Changed

- BREAKING: Kotlin API changed

## 0.1.0 - 2021-01-03

### Added

- Provide an API to fetch tide extremes from worldtides.info
