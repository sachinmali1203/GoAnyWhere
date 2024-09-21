buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.1") // Android Gradle Plugin
        classpath("com.google.gms:google-services:4.3.15") // Google services for Firebase
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10") // Kotlin Gradle Plugin
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}