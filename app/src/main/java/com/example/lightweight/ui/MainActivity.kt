package com.example.lightweight.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import androidx.preference.PreferenceManager
import com.example.lightweight.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private val logTag = "MainActivity"

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Set the theme to light or dark based on the theme preference
        if (sharedPreferences.getString(
                "theme",
                "System default"
            ) == "System default"
        ) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            Log.d(logTag, "Theme is System default")
        } else if (sharedPreferences.getString(
                "theme",
                "System default"
            ) == "Light"
        ) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Log.d(logTag, "Theme is Light")
        } else if (sharedPreferences.getString(
                "theme",
                "System default"
            ) == "Dark"
        ) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Log.d(logTag, "Theme is Dark")
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        drawerLayout = findViewById(R.id.drawer_layout)

        // Passing the top-level fragments
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.selectTrainingCycleFragment,
                R.id.settingsFragment,
            ),
            drawerLayout, // This puts the hamburger icon into the toolbar
        )

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // Set the toolbar to work as the action bar

        // Sets up action bar with navController, with appBarConfiguration controlling how the
        // navigation button is displayed
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)

        // If the keepScreenOn preference is set to true, ensure the display does not sleep
        if (sharedPreferences.getBoolean("keepScreenOn", false)) {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    /**
     * Provides back navigation functionality for back button on toolbar. If back navigation is
     * unsuccessful then superclass function is called.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Navigates to the destination clicked. If this is unsuccessful then base function is called.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}