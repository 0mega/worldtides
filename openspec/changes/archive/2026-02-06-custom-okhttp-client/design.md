# Custom OkHttpClient Design

## 1. Implementation Plan

The implementation will follow a refined approach that keeps the networking logic separate from the builder configuration, ensuring a clean separation of concerns.

### 1.1. `WorldTides.Builder` Modifications

The `WorldTides.Builder` class in `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/WorldTides.kt` will be modified to act as a configuration holder, delegating the client creation to `RetrofitClient`.

1.  A new private nullable property `okHttpClient: OkHttpClient?` will be added, initialized to `null`.
2.  A new private property `loggingLevel: HttpLoggingInterceptor.Level` will be added, initialized to `HttpLoggingInterceptor.Level.NONE`.
3.  A new public method `client(client: OkHttpClient): Builder` will be added. This method will set the `okHttpClient` property and return the `Builder` instance.
4.  A new public method `loggingLevel(level: HttpLoggingInterceptor.Level): Builder` will be added. This method will set the `loggingLevel` property and return the `Builder` instance.
5.  The `build(apiKey: String): WorldTides` method will be updated:
    *   It will instantiate `RetrofitClient`, passing the `okHttpClient` and `loggingLevel` properties to its constructor.
    *   It will then use the `RetrofitClient` instance to get the `tidesService` (the Retrofit gateway).

This keeps the `Builder` clean and focused on configuration.

### 1.2. `RetrofitClient.kt` Modifications

The `worldtides/src/main/kotlin/com/oleksandrkruk/worldtides/RetrofitClient.kt` class will be kept and modified to handle the client creation logic.

1.  The constructor of `RetrofitClient` will be updated to accept an optional `customClient: OkHttpClient?` and a `loggingLevel: HttpLoggingInterceptor.Level`.
2.  Inside the `tidesService` lazy property, the logic for creating the `OkHttpClient` will be as follows:
    *   If `customClient` is not `null`, it will be used.
    *   If `customClient` is `null`, a new default `OkHttpClient` will be built. This client will include an `HttpLoggingInterceptor` with its level set to the provided `loggingLevel`.
    *   The final client will be used to build the `Retrofit` instance.

This isolates all networking setup within the `RetrofitClient` class.

### 1.3. Object Creation Flow

The updated object creation flow will be:

```
WorldTides.Builder
      │
      ├─ client(OkHttpClient)
      ├─ loggingLevel(Level)
      │
      ▼ .build(apiKey)
      │
      │ 1. Pass config (custom client, logging level) to RetrofitClient's constructor
      │
      └─> RetrofitClient
            │
            │ 1. Create OkHttpClient (use custom or build default)
            │ 2. Create Retrofit service
            │
            ▼
      WorldTidesRepository
      │
      ▼
WorldTides instance
```

## 2. Testing Plan

-   **Unit Test 1:** Verify that when a custom `OkHttpClient` is provided to the `RetrofitClient` constructor, it is used.
-   **Unit Test 2:** Verify that when a `loggingLevel` is provided to the `RetrofitClient` constructor, the default `OkHttpClient` is created with the correct interceptor level.
-   **Unit Test 3:** Verify that when both a custom `OkHttpClient` and a `loggingLevel` are provided, the custom client takes precedence.
-   **Unit Test 4:** Verify the default behavior (no custom client, `NONE` logging level) is maintained.
**Integration Test:** Test the full flow from `WorldTides.Builder` to ensure the configuration is passed correctly to `RetrofitClient`.