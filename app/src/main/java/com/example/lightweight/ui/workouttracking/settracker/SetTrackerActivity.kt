package com.example.lightweight.ui.workouttracking.settracker

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
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
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModelFactory
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.workouttracking.settracker.logsets.LogSetsViewModel
import com.example.lightweight.ui.workouttracking.settracker.logsets.LogSetsViewModelFactory
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModelFactory
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModel
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SetTrackerActivity : AppCompatActivity(), KodeinAware {

    val args: SetTrackerActivityArgs by navArgs()

    override val kodein by kodein()
    private val exerciseFactory: ExerciseViewModelFactory by instance()
    private val workoutFactory: WorkoutViewModelFactory by instance()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()
    private val logSetsViewModelFactory: LogSetsViewModelFactory by instance()

    private val exerciseViewModel: ExerciseViewModel by viewModels { exerciseFactory }
    private val workoutViewModel: WorkoutViewModel by viewModels { workoutFactory }
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
        exerciseInstanceFactory
    }
    private val trainingSetViewModel: TrainingSetViewModel by viewModels { trainingSetFactory }
    private val logSetsViewModel: LogSetsViewModel by viewModels { logSetsViewModelFactory }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbarSetTracker: Toolbar
    private lateinit var bottomNavSetTracker: BottomNavigationView

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = LightweightFragmentFactory(
            exerciseViewModel = exerciseViewModel,
            workoutViewModel = workoutViewModel,
            exerciseInstanceViewModel = exerciseInstanceViewModel,
            trainingSetViewModel = trainingSetViewModel,
            logSetsViewModel = logSetsViewModel
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_tracker)

        // Lock the orientation to portrait
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.logSetsFragment,
                R.id.stopwatchFragment,
                R.id.exerciseHistoryFragment,
                R.id.exerciseInsightsFragment
            )
        )

        toolbarSetTracker = findViewById(R.id.toolbar_set_tracker)
        setSupportActionBar(toolbarSetTracker)
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavSetTracker = findViewById(R.id.bottom_nav_set_tracker)
        bottomNavSetTracker.setupWithNavController(navController)
    }
}