plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
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
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.jetbrains.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.websockets)
    implementation(libs.ktor.server.websockets)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}