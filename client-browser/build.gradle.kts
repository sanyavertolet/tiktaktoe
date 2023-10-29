plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.serialization)
    id("com.sanyavertolet.tiktaktoe.buildutils.code-quality-convention")
}

val kotlinWrappersVersion = "1.0.0-pre.636"

repositories {
    mavenCentral()
    maven {
        name = "sanyavertolet/kotlin-js-preview-idea-plugin"
        url = uri("https://maven.pkg.github.com/sanyavertolet/kotlin-js-preview-idea-plugin")
    }
}

kotlin {
    js {
        browser {
            runTask(Action { devServer = devServer?.copy(port = 8082) })
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(projects.api)

                implementation(platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom")

                implementation("org.jetbrains.kotlin-wrappers:kotlin-mui")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-mui-icons")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")

                implementation("com.sanyavertolet.kotlinjspreview:core:0.2.0")
            }
        }
    }
}
