# Tasks

## 1. Core Implementation

- [x] 1.1 Modify the `RetrofitClient` constructor to accept `customClient: OkHttpClient?` and `loggingLevel: HttpLoggingInterceptor.Level`.
- [x] 1.2 Implement the client creation logic within the `RetrofitClient`'s `tidesService` property.
- [x] 1.3 Add `okHttpClient` and `loggingLevel` properties to `WorldTides.Builder`.
- [x] 1.4 Add the public `client()` and `loggingLevel()` methods to `WorldTides.Builder`.
- [x] 1.5 Update the `build()` method in `WorldTides.Builder` to pass its configuration to the `RetrofitClient` constructor.

## 2. Testing

- [x] 2.1 Create the test file `worldtides/src/test/kotlin/com/oleksandrkruk/worldtides/RetrofitClientTest.kt`.
- [x] 2.2 Write a test to verify a user-provided `OkHttpClient` is used.
- [x] 2.3 Write a test to verify the `loggingLevel` is correctly applied.
- [x] 2.4 Write a test to verify a custom client takes precedence over a logging level.
- [x] 2.5 Write a test to verify the default behavior is maintained with no custom configuration.
