# WorldTides Client

[![build status](https://jitpack.io/v/com.oleksandrkruk/worldtides.svg)](https://jitpack.io/#com.oleksandrkruk/worldtides) [![CI](https://github.com/0mega/worldtides/actions/workflows/main.yml/badge.svg)](https://github.com/0mega/worldtides/actions/workflows/main.yml)

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
    implementation 'com.oleksandrkruk:worldtides:2.0.0'
}
```

## Supported WorldTides API calls

| API Request  | API version | Supported | Planned |
| -----------  | ----------- | --------- | ------- |
| Extremes     | v2          | Yes       | -       |
| Heights      | v2          | Yes       | -       |
| Stations     | v2          | No        | Yes     |
| Datum        | v2          | No        | Yes     |

## Usage

### Get Tide Extremes

<details open>
<summary>Kotlin</summary>

```kotlin
val worldTides = WorldTides.Builder().build(apiKey)
worldTides.getTideExtremes(date, days, lat, lon) { result ->
    result.onSuccess { tideExtremes ->
        tideExtremes.extremes.forEach { extreme ->
            println("${extreme.type} at ${extreme.date}: ${extreme.height}m")
        }
    }
    result.onFailure { error ->
        println("Error: ${error.message}")
    }
}
```

</details>

<details>
<summary>Java</summary>

```java
WorldTides wt = new WorldTides.Builder().build(apiKey);
wt.getTideExtremes(date, 7, lat, lon, new TidesCallback<TideExtremes>() {
    @Override
    public void result(TideExtremes tides) {
        // Use the tide extremes
    }

    @Override
    public void error(Error error) {
        // Handle error
    }
});
```

</details>

### Get Tide Heights

<details open>
<summary>Kotlin</summary>

```kotlin
val worldTides = WorldTides.Builder().build(apiKey)
worldTides.getTideHeights(date, days, lat, lon) { result ->
    result.onSuccess { tideHeights ->
        tideHeights.heights.forEach { height ->
            println("Height at ${height.date}: ${height.height}m")
        }
    }
    result.onFailure { error ->
        println("Error: ${error.message}")
    }
}
```

</details>

<details>
<summary>Java</summary>

```java
WorldTides wt = new WorldTides.Builder().build(apiKey);
wt.getTideHeights(date, 7, lat, lon, new TidesCallback<TideHeights>() {
    @Override
    public void result(TideHeights heights) {
        // Use the tide heights
    }

    @Override
    public void error(Error error) {
        // Handle error
    }
});
```

</details>

### Get Flexible Tide Data (Combined)

Fetch multiple data types in a single API call:

<details open>
<summary>Kotlin</summary>

```kotlin
val dataTypes = listOf(TideDataType.HEIGHTS, TideDataType.EXTREMES)
worldTides.getTides(date, days, lat, lon, dataTypes) { result ->
    result.onSuccess { tides ->
        tides.heights?.let { println("Heights: ${it.heights.size} points") }
        tides.extremes?.let { println("Extremes: ${it.extremes.size} points") }
    }
}
```

</details>

<details>
<summary>Java</summary>

```java
List<TideDataType> dataTypes = Arrays.asList(TideDataType.HEIGHTS, TideDataType.EXTREMES);
wt.getTides(date, 7, lat, lon, dataTypes, new TidesCallback<Tides>() {
    @Override
    public void result(Tides tides) {
        if (tides.getHeights() != null) {
            // Use heights data
        }
        if (tides.getExtremes() != null) {
            // Use extremes data
        }
    }

    @Override
    public void error(Error error) {
        // Handle error
    }
});
```

</details>

## Versioning

This project follows [Semantic Versioning](https://semver.org/).
