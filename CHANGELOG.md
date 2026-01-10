# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## UNRELEASED

### Added

- **Heights API**: New `getTideHeights()` method to fetch tide height predictions
- **Flexible Tides API**: New `getTides()` method to fetch multiple data types in a single call
- `TideDataType` enum for specifying which data types to request (HEIGHTS, EXTREMES)
- `Tides` model for combined API responses
- `TideHeights` and `Height` models for tide height data
- Comprehensive test coverage for new API methods
- SpekKit
- Project constitution
- Github copilot instructions for commit summaries

### Changed

- Refactored `WorldTidesRepository` and `WorldTidesGateway` to package root for shared usage
- Bump Kotlin version to 1.9.20
- Bump Gradle to 8.9
- Bump mockito to 5.13.0
- Bump to JDK_1_8
- Replace jcenter() with mavenCentral()
- Update github actions setup

### Breaking Changes

- **BREAKING**: `TidesCallback` is now generic `TidesCallback<T>`. Existing consumers using Java-style callbacks must update to `TidesCallback<TideExtremes>`

## 1.0.0 - 2021-02-15

### Added

- Compatibility with Java

### Changed

- BREAKING: Kotlin API changed

## 0.1.0 - 2021-01-03

### Added

- Provide an API to fetch tide extremes from worldtides.info
