rootProject.name = "firebase_app_installations"

apply(from = "local-config.gradle.kts")

val androidGradlePluginVersion: String by settings

pluginManagement {
    plugins {
        id("com.android.application") version androidGradlePluginVersion
        id("com.android.library") version androidGradlePluginVersion
    }
}