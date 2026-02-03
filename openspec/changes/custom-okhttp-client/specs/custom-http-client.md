## ADDED Requirements

### Requirement: Provide custom OkHttpClient
The library SHALL allow users to provide their own `OkHttpClient` instance.

#### Scenario: User provides a custom client
- **WHEN** a user configures the `WorldTides.Builder` with a custom `OkHttpClient`
- **THEN** the library MUST use this client for all network requests.

### Requirement: Configure logging level
The library SHALL allow users to configure the logging level for the default `OkHttpClient`.

#### Scenario: User sets a logging level
- **WHEN** a user configures the `WorldTides.Builder` with a specific `HttpLoggingInterceptor.Level`
- **AND** no custom `OkHttpClient` is provided
- **THEN** the library's default `OkHttpClient` MUST use that logging level.

#### Scenario: User sets logging level AND provides a custom client
- **WHEN** a user configures the `WorldTides.Builder` with both a logging level and a custom `OkHttpClient`
- **THEN** the library MUST use the user-provided `OkHttpClient` and ignore the logging level setting.

### Requirement: Maintain backward compatibility
All new configuration options SHALL be optional, preserving existing behavior.

#### Scenario: User does not provide any custom configuration
- **WHEN** a user initializes `WorldTides` without providing a custom client or logging level
- **THEN** the library MUST operate with the default `OkHttpClient` and logging set to `NONE`, exactly as it did before this change.
