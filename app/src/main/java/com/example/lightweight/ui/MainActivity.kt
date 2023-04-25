package com.example.lightweight.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import androidx.preference.PreferenceManager
import com.example.lightweight.R
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.category.CategoryViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModel
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModel
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModelFactory
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModelFactory
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.workouttracking.home.HomeViewModel
import com.example.lightweight.ui.workouttracking.home.HomeViewModelFactory
import com.example.lightweight.ui.workouttracking.selectcategory.SelectCategoryViewModel
import com.example.lightweight.ui.workouttracking.selectcategory.SelectCategoryViewModelFactory
import com.example.lightweight.ui.workouttracking.selectexercise.SelectExerciseViewModel
import com.example.lightweight.ui.workouttracking.selectexercise.SelectExerciseViewModelFactory
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModelFactory
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModel
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModelFactory
import com.google.android.material.navigation.NavigationView
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    private val logTag = "MainActivity"

    override val kodein by kodein()
    private val categoryFactory: CategoryViewModelFactory by instance()
    private val exerciseFactory: ExerciseViewModelFactory by instance()
    private val workoutFactory: WorkoutViewModelFactory by instance()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()
    private val cycleFactory: CycleViewModelFactory by instance()
    private val cycleDayFactory: CycleDayViewModelFactory by instance()
    private val cycleDayCategoryFactory: CycleDayCategoryViewModelFactory by instance()
    private val cycleDayExerciseFactory: CycleDayExerciseViewModelFactory by instance()
    private val homeViewModelFactory: HomeViewModelFactory by instance()
    private val selectCategoryViewModelFactory: SelectCategoryViewModelFactory by instance()
    private val selectExerciseViewModelFactory: SelectExerciseViewModelFactory by instance()

    private val categoryViewModel: CategoryViewModel by viewModels { categoryFactory }
    private val exerciseViewModel: ExerciseViewModel by viewModels { exerciseFactory }
    private val workoutViewModel: WorkoutViewModel by viewModels { workoutFactory }
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
        exerciseInstanceFactory
    }
    private val trainingSetViewModel: TrainingSetViewModel by viewModels { trainingSetFactory }
    private val cycleViewModel: CycleViewModel by viewModels { cycleFactory }
    private val cycleDayViewModel: CycleDayViewModel by viewModels { cycleDayFactory }
    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel by viewModels {
        cycleDayCategoryFactory
    }
    private val cycleDayExerciseViewModel: CycleDayExerciseViewModel by viewModels {
        cycleDayExerciseFactory
    }
    private val homeViewModel: HomeViewModel by viewModels {
        homeViewModelFactory
    }
    private val selectCategoryViewModel: SelectCategoryViewModel by viewModels {
        selectCategoryViewModelFactory
    }
    private val selectExerciseViewModel: SelectExerciseViewModel by viewModels {
        selectExerciseViewModelFactory
    }

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = LightweightFragmentFactory(
            categoryViewModel,
            exerciseViewModel,
            workoutViewModel,
            exerciseInstanceViewModel,
            trainingSetViewModel,
            cycleViewModel,
            cycleDayViewModel,
            cycleDayCategoryViewModel,
            cycleDayExerciseViewModel,
            homeViewModel,
            selectCategoryViewModel,
            selectExerciseViewModel,
        )

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