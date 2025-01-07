import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "2.1.0"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm()

    js(IR) {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json2)
            implementation(libs.ktor.ktor.client.core)
            implementation(libs.ktor.ktor.client.cio)
            implementation(libs.ktor.ktor.client.serialization)
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.1")
            implementation(libs.ktor.ktor.client.logging)
            implementation(libs.io.ktor.ktor.client.content.negotiation2)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)

        }
    }

}

android {
    namespace = "es.gaspardev.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
