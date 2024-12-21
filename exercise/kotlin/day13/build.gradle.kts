import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.21"
    application
    id("info.solidsoft.pitest") version "1.15.0"
}

repositories {
    mavenCentral()
}

dependencies {
    val kotestVersion = "5.8.0"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")
    testImplementation(kotlin("test"))

    testImplementation("io.kotest.extensions:kotest-extensions-pitest:1.2.0")
}

pitest {
    targetClasses.set(setOf("santamarket.model.*")) //by default "${project.group}.*"
    pitestVersion.set("1.12.0") //not needed when a default PIT version should be used
    threads.set(4)
    outputFormats.set(setOf("XML", "HTML"))
    timestampedReports.set(false)
    verbose.set(true)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}