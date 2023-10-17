rootProject.name = "tiktaktoe"

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
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("gradle/plugins")
include("server")
include("client")
include("common")
