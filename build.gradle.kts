group = "com.sanyavertolet.tiktaktoe"
description = "Kotlin Tic Tac Toe"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    alias(libs.plugins.dokka)
    id("com.sanyavertolet.tiktaktoe.buildutils.code-quality-convention")
    id("com.sanyavertolet.tiktaktoe.buildutils.versioning-configuration")
}
