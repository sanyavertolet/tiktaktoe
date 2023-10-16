plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    id("com.sanyavertolet.tiktaktoe.buildutils.code-quality-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.logback.classic)
    implementation(libs.ktor.serialization.kotlinx.json)
}
