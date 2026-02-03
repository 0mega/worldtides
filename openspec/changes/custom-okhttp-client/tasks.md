# Tasks

## 1. Core Implementation

- [ ] 1.1 Modify the `RetrofitClient` constructor to accept `customClient: OkHttpClient?` and `loggingLevel: HttpLoggingInterceptor.Level`.
- [ ] 1.2 Implement the client creation logic within the `RetrofitClient`'s `tidesService` property.
- [ ] 1.3 Add `okHttpClient` and `loggingLevel` properties to `WorldTides.Builder`.
- [ ] 1.4 Add the public `client()` and `loggingLevel()` methods to `WorldTides.Builder`.
- [ ] 1.5 Update the `build()` method in `WorldTides.Builder` to pass its configuration to the `RetrofitClient` constructor.

## 2. Testing

- [ ] 2.1 Create the test file `worldtides/src/test/kotlin/com/oleksandrkruk/worldtides/RetrofitClientTest.kt`.
- [ ] 2.2 Write a test to verify a user-provided `OkHttpClient` is used.
- [ ] 2.3 Write a test to verify the `loggingLevel` is correctly applied.
- [ ] 2.4 Write a test to verify a custom client takes precedence over a logging level.
- [ ] 2.5 Write a test to verify the default behavior is maintained with no custom configuration.
