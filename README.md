# WorldTides Client

[![build status](https://jitpack.io/v/com.oleksandrkruk/worldtides.svg)](https://jitpack.io/#com.oleksandrkruk/worldtides)

Client for [World Tides API](https://www.worldtides.info/apidocs) compatible with Android, Kotlin and Java applications.

## Download

Add the JitPack repo to your root `build.gradle`:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Declare the dependency in the module's `build.gradle`:

```gradle
dependencies {
    implementation 'com.oleksandrkruk:worldtides:1.0.0'
}
```

## Supported WorldTides API calls

| API Request  | API version | Supported | Planned |
| -----------  | ----------- | --------- | ------- |
| Extremes     | v2          | Yes       | Yes     |
| Heights      | v2          | No        | Yes     |
| Stations     | v2          | No        | Yes     |
| Datum        | v2          | No        | Yes     |

## Usage

Following snippet demonstrates how to use world tides lib to fetch tides extremes from [worldtides.info](https://www.worldtides.info/apidocs).

<details open>
<summary>Get Tide Extremes - Kotlin</summary>

```Kotlin
    val worldTides = WorldTides.Builder().build(apiKey)
    worldTides.getTideExtremes(date, days, lat, lon, object : TidesCallback {
        override fun result(result: Result<TideExtremes>) {
            result.onSuccess { tideExtremes ->
                // use the tide extremes here as you wish
            }
        }
    })
```

</details>

<details>
<summary>Get Tide Extremes - Java</summary>

```Java
    WorldTides wt = (new WorldTides.Builder()).build(apiKey);
    wt.getTideExtremes(date, 1, latitude, longitude, new TidesCallback() {
        @Override
        public void onResult(@NotNull TideExtremes tides) {
            // Use the tide extremes
        }

        @Override
        public void onError() {
            // Report an error
        }
    });
```

</details>

## Versioning

This project follows [Semantic Versioning](https://semver.org/).

## Limitations

- This lib uses Retrofit and OkHttp and at the moment it's not possible to pass in you existing OkHttp or other client to be used by retrofit. The plan is to allow the user of this lib to pass in the client to avoid creating multiple clients in your app.
