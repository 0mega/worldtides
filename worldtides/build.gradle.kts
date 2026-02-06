
plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.9.20"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // Publishing
    `maven-publish`

    // Tests results logging
    id("com.adarshr.test-logger") version "2.1.1"
}

repositories {
    // Use Maven central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.72")

    // Retrofit 2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.okhttp3:okhttp:3.12.0")
    api("com.squareup.okhttp3:logging-interceptor:3.12.0")
    // Moshi converter for Retrofit
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")

    // Use the Kotlin test library.
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:3.12.0")
    testImplementation("org.mockito:mockito-core:5.13.0")
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

tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

tasks.compileJava {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
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
