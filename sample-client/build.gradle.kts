plugins {
    kotlin("jvm") version "1.9.20"
    application
}

repositories {
    mavenLocal()  // Consume worldtides from local publish
    mavenCentral()
}

dependencies {
    implementation("com.oleksandrkruk:worldtides:+")  // Latest from mavenLocal

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

application {
    mainClass.set("com.oleksandrkruk.worldtides.sample.SampleClientKt")
}

tasks.test {
    useJUnitPlatform()
    // Pass environment variable to tests
    environment("WORLD_TIDES_API_KEY", System.getenv("WORLD_TIDES_API_KEY") ?: "")

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showCauses = true
        showStackTraces = true
    }

    // Show test duration
    afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
        if (desc.parent == null) {
            println("\nTest Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped)")
            println("Duration: ${result.endTime - result.startTime}ms")
        }
    }))
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

