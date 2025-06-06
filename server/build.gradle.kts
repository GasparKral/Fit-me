plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "2.1.0"
    application
}

group = "es.gaspardev"
version = "1.0.0"
application {
    mainClass.set("es.gaspardev.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.compression)
    implementation(libs.exposed.core.v0610)
    implementation(libs.exposed.dao.v0610)
    runtimeOnly(libs.exposed.jdbc.v0610)
    implementation(libs.exposed.java.time.v0610)
    implementation(libs.postgresql.v4275)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.websockets)
    implementation(libs.ktor.server.websockets)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}