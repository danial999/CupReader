plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    // Apply Google Services and Crashlytics plugins
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")


}

android {
    namespace = "com.example.cupreader"
    compileSdk = 35



    defaultConfig {
        applicationId = "com.example.cupreader"
        minSdk = 33
        targetSdk = 35
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
    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        // match your Compose BOM’s compiler version
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    kotlinOptions {
        jvmTarget = "11"
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        // enable desugaring
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.com.google.firebase.firebase.auth)

    // Crashlytics & Analytics via BoM
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)


    //splash
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.accompanist.systemuicontroller)

    // Compose & AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core.android)
    // Material & Views
    implementation(libs.material.v1110)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Google Sign-In
    implementation(libs.play.services.auth)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // AI
    implementation(libs.generativeai)

    // Credential Manager
    implementation(libs.androidx.credentials.v130)
    implementation(libs.androidx.credentials.play.services.auth.v130)
    implementation(libs.googleid)
    implementation(libs.androidx.datastore.preferences.core.android)
    implementation(libs.ads.mobile.sdk)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // Desugaring support
    coreLibraryDesugaring(libs.desugar.jdk.libs)

}
