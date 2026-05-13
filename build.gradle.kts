// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Android application plugin.
    alias(libs.plugins.android.application) apply false
    // Kotlin Compose plugin for Jetpack Compose support.
    alias(libs.plugins.kotlin.compose) apply false
}