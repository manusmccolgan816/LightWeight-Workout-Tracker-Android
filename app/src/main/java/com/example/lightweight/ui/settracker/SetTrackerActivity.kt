package com.example.lightweight.ui.settracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lightweight.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SetTrackerActivity : AppCompatActivity() {

    private val args: SetTrackerActivityArgs by navArgs()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbarSetTracker: Toolbar
    private lateinit var bottomNavSetTracker: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_tracker)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.logSetsFragment, R.id.stopwatchFragment, R.id.exerciseHistoryFragment)
        )

        toolbarSetTracker = findViewById(R.id.toolbar_set_tracker)
        setSupportActionBar(toolbarSetTracker)
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavSetTracker = findViewById(R.id.bottom_nav_set_tracker)
        bottomNavSetTracker.setupWithNavController(navController)
    }
}