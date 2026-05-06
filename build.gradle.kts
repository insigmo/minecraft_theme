import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

plugins {
    kotlin("jvm") version "2.1.21"
    id("org.jetbrains.intellij.platform") version "2.13.0"
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdea("2025.1")
    }
}

intellijPlatform {
    pluginConfiguration {
        name = providers.gradleProperty("pluginName").get()
        version = providers.gradleProperty("pluginVersion").get()

        ideaVersion {
            sinceBuild = "251"
            untilBuild = provider { null }
        }
    }
}

kotlin {
    jvmToolchain(21)
}

