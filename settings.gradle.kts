rootProject.name = "tiktaktoe"

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven {
            name = "sanyavertolet/kotlin-js-preview-idea-plugin"
            url = uri("https://maven.pkg.github.com/sanyavertolet/kotlin-js-preview-idea-plugin")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.10" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.10" apply false
    id("com.jakewharton.mosaic") version "0.9.1" apply false
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("gradle/plugins")
include("common")
include("api")
include("server")
include("client-cli")
include("client-browser")
