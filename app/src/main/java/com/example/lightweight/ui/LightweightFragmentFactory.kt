package com.example.lightweight.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.settracker.logsets.LogSetsFragment
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModel

class LightweightFragmentFactory(
    private val categoryViewModel: CategoryViewModel?,
    private val exerciseViewModel: ExerciseViewModel?,
    private val workoutViewModel: WorkoutViewModel?,
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel?,
    private val trainingSetViewModel: TrainingSetViewModel?
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            LogSetsFragment::class.java.name -> {
                LogSetsFragment(exerciseViewModel!!, workoutViewModel!!, exerciseInstanceViewModel!!, trainingSetViewModel!!)
//                if (exerciseViewModel != null && workoutViewModel != null
//                    && exerciseInstanceViewModel != null && trainingSetViewModel != null) {
//                    LogSetsFragment(exerciseViewModel, workoutViewModel, exerciseInstanceViewModel, trainingSetViewModel)
//                } else {
//                    super.instantiate(classLoader, className)
//                }
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}