buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.gradle.kotlin)
        classpath(libs.gradle.hilt)
        classpath(libs.gradle.safe.args)
        classpath(libs.androidx.room.gradle.plugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization.plugin)

}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}