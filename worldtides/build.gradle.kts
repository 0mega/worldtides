
plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.72"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // Tests results logging
    id("com.adarshr.test-logger") version "2.1.1"
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

group = "com.oleksandrkruk"
version = "0.1.0"

// This will set project and version in MANIFEST.MF
tasks.jar {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version)
        )
    }
}
