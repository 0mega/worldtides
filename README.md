[![](https://jitpack.io/v/com.oleksandrkruk/worldtides.svg)](https://jitpack.io/#com.oleksandrkruk/worldtides)

# WorldTides Client
Kotlin Client for [World Tides API](https://www.worldtides.info/apidocs)

## Download

Add the JitPack repo to your root `build.gradle`:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Declare the dependency in the module `build.gradle`:

```
dependencies {
    implementation 'com.oleksandrkruk:worldtides:0.1.0'
}
```

## Usage

An example demonstrating how to use world tides lib to fetch tides extremes from [worldtides.info](https://www.worldtides.info/apidocs).

### Get Tide Extremes

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

## Limitations

- Currently this project is incompatible with Java due to the use of Kotlin [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/) type.
The plan is to introduce a Java-friendly API in future.
- This lib uses Retrofit and OkHttp and at the moment it's not possible to pass in you existing OkHttp or other client
to be used by retrofit. The plan is to allow the user of the lib to pass in the client to avoid creating multiple
clients in your app.
