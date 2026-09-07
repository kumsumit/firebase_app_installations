import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

group = "io.flutter.plugins.firebase.installations.firebase_app_installations"
version = "1.0"

plugins {
    id("com.android.library")
}

apply(from = "local-config.gradle.kts")

rootProject.allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val firebaseCoreProject =
    findProject(":firebase_core")
        ?: throw GradleException(
            "Could not find the firebase_core FlutterFire plugin, " +
                "have you added it as a dependency in your pubspec?"
        )

if (firebaseCoreProject.findProperty("FirebaseSDKVersion") == null) {
    throw GradleException(
        "A newer version of the firebase_core FlutterFire plugin is required, " +
            "please update your firebase_core pubspec dependency."
    )
}

fun getRootProjectExtOrCoreProperty(
    name: String,
    firebaseCoreProject: Project,
): Any {
    val flutterFire =
        rootProject.extensions.extraProperties
            .properties["FlutterFire"] as? Map<*, *>

    return flutterFire?.get(name)
        ?: firebaseCoreProject.findProperty(name)
        ?: throw GradleException("Property '$name' not found")
}

val compileSdkValue = extra["compileSdk"] as Int
val minSdkValue = extra["minSdk"] as Int
val javaVersion = extra["javaVersion"] as JavaVersion

extensions.configure<LibraryExtension> {
    namespace = "io.flutter.plugins.firebase.installations.firebase_app_installations"
    compileSdk = compileSdkValue

    defaultConfig {
        minSdk = minSdkValue
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(javaVersion.toString())
    }
}

dependencies {
    add("api", firebaseCoreProject)
    add(
        "implementation",
        platform(
            "com.google.firebase:firebase-bom:${getRootProjectExtOrCoreProperty("FirebaseSDKVersion", firebaseCoreProject)}"
        )
    )
    add("implementation", "com.google.firebase:firebase-installations")
    add("implementation", "androidx.annotation:annotation:1.10.0")
}

apply(from = "user-agent.gradle.kts")