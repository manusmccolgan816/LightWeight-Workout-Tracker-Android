package com.example.lightweight.ui

import com.example.lightweight.ui.cycleplanning.selecttrainingcycle.SelectTrainingCycleTest
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.ViewTrainingCycleFragmentTest
import com.example.lightweight.ui.settings.SettingsFragmentTest
import com.example.lightweight.ui.workouttracking.calendar.CalendarFragmentTest
import com.example.lightweight.ui.workouttracking.home.HomeFragmentTest
import com.example.lightweight.ui.workouttracking.selectcategory.SelectCategoryFragmentTest
import com.example.lightweight.ui.workouttracking.selectexercise.SelectExerciseFragmentTest
import com.example.lightweight.ui.workouttracking.settracker.SetTrackerActivityTest
import com.example.lightweight.ui.workouttracking.settracker.exercisehistory.ExerciseHistoryFragmentTest
import com.example.lightweight.ui.workouttracking.settracker.exerciseinsights.ExerciseInsightsFragmentTest
import com.example.lightweight.ui.workouttracking.settracker.logsets.LogSetsFragmentTest
import com.example.lightweight.ui.workouttracking.settracker.stopwatch.StopwatchFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityTest::class,
    SetTrackerActivityTest::class,
    HomeFragmentTest::class,
    CalendarFragmentTest::class,
    SelectCategoryFragmentTest::class,
    SelectExerciseFragmentTest::class,
    LogSetsFragmentTest::class,
    StopwatchFragmentTest::class,
    ExerciseHistoryFragmentTest::class,
    ExerciseInsightsFragmentTest::class,
    SettingsFragmentTest::class,
    SelectTrainingCycleTest::class,
    ViewTrainingCycleFragmentTest::class
)
class UITestSuite