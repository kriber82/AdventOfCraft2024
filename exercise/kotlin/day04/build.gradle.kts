import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.21"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    val kotestVersion = "5.8.0"
    val mockkVersion = "1.13.3"

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-agent-jvm:$mockkVersion")
}

tasks.test {
    useJUnitPlatform()

    doFirst {
        println("Running tests with JVM: ${System.getProperty("java.version")}")
    }
}

kotlin {
    jvmToolchain(20)

    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_20)
    }
}