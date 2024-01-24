// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.21"))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.49")
    }
}
