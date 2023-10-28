plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    id("com.jakewharton.mosaic")
    id("com.sanyavertolet.tiktaktoe.buildutils.code-quality-convention")
    id("org.jetbrains.compose") version "1.5.3"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(projects.api)
    implementation(compose.desktop.currentOs)

    implementation(libs.jline)
    implementation(libs.clikt)

    implementation(libs.ktor.client.cio)
    implementation(libs.logback.classic)

    testImplementation(libs.kotlin.test.junit)
}

compose.desktop {
    application {
        mainClass = "com.sanyavertolet.tiktaktoe.ApplicationKt"
    }
}
