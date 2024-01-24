package com.example.recipeapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.recipeapp.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
/**
 * Main activity for the application, annotated with AndroidEntryPoint for Hilt integration.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /**
     * Overrides the default [AppCompatActivity.onCreate] method.
     * Sets up the UI components, navigation, and handles destination changes.
     *
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Log activity creation
        Timber.d("Activity was created")
        // Inflate and set the content view using View Binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up navigation using NavController and BottomNavigationView
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
    }
}
