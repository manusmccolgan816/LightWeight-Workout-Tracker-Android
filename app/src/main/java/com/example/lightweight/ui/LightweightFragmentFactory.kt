package com.example.lightweight.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModel
import com.example.lightweight.ui.cycleplanning.selecttrainingcycle.SelectTrainingCycleFragment
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.ViewTrainingCycleFragment
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.workouttracking.calendar.CalendarFragment
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.home.HomeFragment
import com.example.lightweight.ui.workouttracking.home.HomeViewModel
import com.example.lightweight.ui.workouttracking.selectcategory.SelectCategoryFragment
import com.example.lightweight.ui.workouttracking.selectexercise.SelectExerciseFragment
import com.example.lightweight.ui.workouttracking.settracker.exercisehistory.ExerciseHistoryFragment
import com.example.lightweight.ui.workouttracking.settracker.exerciseinsights.ExerciseInsightsFragment
import com.example.lightweight.ui.workouttracking.settracker.logsets.LogSetsFragment
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModel

class LightweightFragmentFactory(
    private val categoryViewModel: CategoryViewModel? = null,
    private val exerciseViewModel: ExerciseViewModel? = null,
    private val workoutViewModel: WorkoutViewModel? = null,
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel? = null,
    private val trainingSetViewModel: TrainingSetViewModel? = null,
    private val cycleViewModel: CycleViewModel? = null,
    private val cycleDayViewModel: CycleDayViewModel? = null,
    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel? = null,
    private val cycleDayExerciseViewModel: CycleDayExerciseViewModel? = null,
    private val homeViewModel: HomeViewModel? = null,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            HomeFragment::class.java.name -> {
                HomeFragment(
                    homeViewModel!!
                )
            }
            CalendarFragment::class.java.name -> {
                CalendarFragment(workoutViewModel!!)
            }
            SelectCategoryFragment::class.java.name -> {
                SelectCategoryFragment(categoryViewModel!!)
            }
            SelectExerciseFragment::class.java.name -> {
                SelectExerciseFragment(
                    categoryViewModel!!,
                    exerciseViewModel!!
                )
            }
            LogSetsFragment::class.java.name -> {
                LogSetsFragment(
                    exerciseViewModel!!,
                    workoutViewModel!!,
                    exerciseInstanceViewModel!!,
                    trainingSetViewModel!!
                )
            }
            ExerciseHistoryFragment::class.java.name -> {
                ExerciseHistoryFragment(exerciseInstanceViewModel!!, trainingSetViewModel!!)
            }
            ExerciseInsightsFragment::class.java.name -> {
                ExerciseInsightsFragment(
                    exerciseInstanceViewModel!!,
                    trainingSetViewModel!!
                )
            }
            SelectTrainingCycleFragment::class.java.name -> {
                SelectTrainingCycleFragment(cycleViewModel!!)
            }
            ViewTrainingCycleFragment::class.java.name -> {
                ViewTrainingCycleFragment(
                    categoryViewModel!!,
                    exerciseViewModel!!,
                    cycleViewModel!!,
                    cycleDayViewModel!!,
                    cycleDayCategoryViewModel!!,
                    cycleDayExerciseViewModel!!
                )
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}