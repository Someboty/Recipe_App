package com.example.recipeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
/**
 * Custom Application class annotated with Hilt for dependency injection.
 */
@HiltAndroidApp
class MyApplication : Application() {
    /**
     * Overrides the default [Application.onCreate] method.
     * Initializes Timber logging in debug mode.
     */
    override fun onCreate() {
        super.onCreate()
        // Plant Timber logger in debug mode
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
