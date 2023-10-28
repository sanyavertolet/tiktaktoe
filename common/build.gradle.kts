plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.serialization)
    id("com.sanyavertolet.tiktaktoe.buildutils.code-quality-convention")
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        browser()
    }
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.ktor.serialization.kotlinx.json)
            }
        }
    }
}
