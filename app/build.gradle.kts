plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.calidhonia.goanywhere"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.calidhonia.goanywhere"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // Core AndroidX libraries
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")

    // Google Maps and Places API
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.android.libraries.places:places:2.5.0")


    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:22.1.1")

    // Volley for networking
    implementation("com.android.volley:volley:1.2.1")

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.google.maps.android:android-maps-utils:2.2.5")

}

// Ensure the repositories are included
repositories {
    google()
    mavenCentral()
}

// Apply Firebase plugin
apply(plugin = "com.google.gms.google-services")
