## Why

To allow library users to customize the underlying `OkHttpClient` for advanced use cases (e.g., custom interceptors, caching) or to easily enable logging for debugging purposes, without being forced to adopt a specific logging framework.

## What Changes

- Add a new method to `WorldTides.Builder` to accept a user-provided `OkHttpClient`.
- Add a new method to `WorldTides.Builder` to set the `HttpLoggingInterceptor.Level` for the default `OkHttpClient`.
- The new methods will not be a breaking change and will be optional.
- The internal `RetrofitClient` will be refactored to support the new customization options.

## Capabilities

### New Capabilities
- `custom-http-client`: Allow library users to provide their own `OkHttpClient` instance or configure the logging level of the default client.

### Modified Capabilities
- None

## Impact

- `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTides.kt`: The `Builder` class will be modified to include the new methods.
- `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/RetrofitClient.kt`: This file will be either modified or removed, and its logic moved into the `WorldTides.Builder`.
