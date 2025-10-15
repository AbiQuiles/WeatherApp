plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization) //Kotlin Serialization
    alias(libs.plugins.devtool.ksp) //Kotlin Annotation Precessing Tool
    alias(libs.plugins.dagger.hilt) //Hilt
    alias(libs.plugins.android.room) //Room
}

android {
    namespace = "com.example.weather"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.weather"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    //Material 3 Icons
    implementation(libs.androidx.material.icons.core)
    //Android Compose ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //Jetpack Compose Navigation
    implementation(libs.navigation.compose)

    //Coil | Doc -> https://coil-kt.github.io/coil/
    implementation(libs.coil.compose) //Our version -> https://coil-kt.github.io/coil/upgrading_to_coil3/
    implementation(libs.coil.network.okhttp) //Coil -> Help with Adding Images through URL

    //Dagger - Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation (libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.lifecycle.viewmodel)// Coroutines Lifecycle Scope

    //Kotlin Serialization Json
    implementation(libs.kotlinx.serialization.json)

    //Room
    implementation(libs.room.runtime.android)
    ksp(libs.room.android.compiler)

    //Retrofit
    implementation(libs.retrofit)
    //OkHttp
    implementation(libs.okhttp)
    //JSON Converter - Square GSON
    implementation(libs.gson)

    //Annotation Processing Tool (Use for Room, Hilt and anything that uses annotations
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}