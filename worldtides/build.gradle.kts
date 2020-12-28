
plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.72"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // Publishing
    `maven-publish`

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

group = project.property("GROUP") as String
version = project.property("VERSION") as String

// This will set project name and version in MANIFEST.MF
tasks.jar {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version)
        )
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("worldtides") {
            from(components["java"])
            withoutBuildIdentifier()

            pom {
                name.set(project.property("POM_NAME") as String)
                description.set(project.property("POM_DESCRIPTION") as String)

                licenses {
                    license {
                        name.set(project.property("POM_LICENSE") as String)
                        url.set(project.property("POM_LICENSE_URL") as String)
                    }
                }

                developers {
                    developers {
                        developer {
                            id.set(project.property("POM_DEV_ID") as String)
                            name.set(project.property("POM_DEV_NAME") as String)
                            email.set(project.property("POM_DEV_EMAIL") as String)
                        }
                    }
                }

                scm {
                    connection.set(project.property("POM_SCM_CONNECTION") as String)
                    developerConnection.set(project.property("POM_SCM_DEV_CONNECTION") as String)
                    url.set(project.property("POM_SCM_URL") as String)
                }
            }
        }
    }
}
