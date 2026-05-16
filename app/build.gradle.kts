plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.nanoporetech.scainter"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.nanoporetech.scainter"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // view model
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // extra icons
    implementation(libs.androidx.compose.material.icons.extended)

    // fontawesome icons
    implementation(libs.font.awesome)

    // navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.core)

    // Credentials Store
    implementation(libs.androidx.security.crypto)
    implementation(libs.play.services.base)

    // coroutine testing
    testImplementation(libs.kotlinx.coroutines.test)

    // Retrofit
    implementation(libs.retrofit)

    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    // Retrofit with Kotlin serialization Converter
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    // Retrofit Gson converter
    implementation(libs.converter.gson)
    implementation(libs.okhttp)

    // Logging interceptor
    implementation(libs.logging.interceptor)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // Firebase Cloud Messaging
    implementation(libs.firebase.messaging)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}