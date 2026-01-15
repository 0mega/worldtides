# Quickstart: Tide Data Requests

## Fetch Tide Heights (Kotlin)

```kotlin
val worldTides = WorldTides.Builder().build(apiKey)

worldTides.getTideHeights(Date(), 7, "51.5074", "-0.1278") { result ->
    result.onSuccess { tideHeights ->
        println("Fetched ${tideHeights.heights.size} height points")
        tideHeights.heights.forEach {
            println("Height at ${it.date}: ${it.height}")
        }
    }
    result.onFailure { error ->
        println("Error: ${error.message}")
    }
}
```

## Fetch Tide Heights (Java)

```java
WorldTides wt = new WorldTides.Builder().build(apiKey);

wt.getTideHeights(new Date(), 7, "51.5074", "-0.1278", new TidesCallback<TideHeights>() {
    @Override
    public void result(TideHeights tides) {
        System.out.println("Fetched " + tides.getHeights().size() + " points");
    }

    @Override
    public void error(Error error) {
        System.out.println("Error: " + error.getMessage());
    }
});
```

---

## Fetch Flexible Tides Data (Kotlin)

Request multiple data types in a single API call:

```kotlin
val dataTypes = listOf(TideDataType.HEIGHTS, TideDataType.EXTREMES)

worldTides.getTides(Date(), 7, "51.5074", "-0.1278", dataTypes) { result ->
    result.onSuccess { tides ->
        tides.heights?.let { println("Heights: ${it.heights.size} points") }
        tides.extremes?.let { println("Extremes: ${it.extremes.size} points") }
    }
    result.onFailure { error ->
        println("Error: ${error.message}")
    }
}
```

## Fetch Flexible Tides Data (Java)

```java
List<TideDataType> dataTypes = Arrays.asList(TideDataType.HEIGHTS, TideDataType.EXTREMES);

wt.getTides(new Date(), 7, "51.5074", "-0.1278", dataTypes, new TidesCallback<Tides>() {
    @Override
    public void result(Tides tides) {
        if (tides.getHeights() != null) {
            System.out.println("Heights: " + tides.getHeights().getHeights().size());
        }
        if (tides.getExtremes() != null) {
            System.out.println("Extremes: " + tides.getExtremes().getExtremes().size());
        }
    }

    @Override
    public void error(Error error) {
        System.out.println("Error: " + error.getMessage());
    }
});
```
