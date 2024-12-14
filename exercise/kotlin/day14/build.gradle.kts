import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("kapt") version "1.6.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    val kotestVersion = "5.8.0"
    val mapStructVersion = "1.5.3.Final"
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.mapstruct:mapstruct:$mapStructVersion")
    implementation ("org.junit.jupiter:junit-jupiter:5.11.3")
    kapt("org.mapstruct:mapstruct-processor:$mapStructVersion")
    //testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    //testImplementation(kotlin("test"))
    testImplementation("com.approvaltests:approvaltests:24.12.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

kapt {
    correctErrorTypes = true
}