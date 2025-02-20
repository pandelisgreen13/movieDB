plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("androidx.room")
}


android {
    compileSdk = 35
    defaultConfig {
        applicationId = "gr.pchasapis.moviedb"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0"
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    namespace = "gr.pchasapis.moviedb"

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(libs.symbol.processing.api)

    // Android Libraries
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.kotlin.serialization)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    implementation(libs.material)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.compose.ktx)
    implementation(libs.lifecycle.viewmodel.compose.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.activity.compose)

    implementation(libs.paging.runtime.ktx)
    implementation(libs.compose.paging)
    // Logging
    implementation(libs.timber)
    // Dialogs
    implementation(libs.dialogCore)
    // Network
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    // Room components
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.runtime.ksp)

    implementation(libs.ui.graphics)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)
    // Image Handling
    implementation(libs.picasso)
    implementation(libs.coil.compose)

    // ViewPager2
    implementation(libs.viewpager2)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.ksp.android)

    implementation(libs.hilt.navigation.compose)

    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Compose
    implementation(libs.androidx.animation.graphics)
    // Integration with activities
    implementation(platform(libs.compose.bom))
    // Compose Material Design
    implementation(libs.androidx.material3)
    // Animations
    implementation(libs.androidx.animation)
    // Tooling support (Previews, etc.)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.runtime.livedata)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3.adaptive.navigation.suite.android)

    // Compose view model
    implementation(libs.androidx.ui)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.core.test)
    testImplementation(libs.kotlinx.coroutines.test)
}
