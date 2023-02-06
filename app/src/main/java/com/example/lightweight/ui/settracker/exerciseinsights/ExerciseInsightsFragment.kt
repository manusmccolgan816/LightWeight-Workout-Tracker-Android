package com.example.lightweight.ui.settracker.exerciseinsights

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.settracker.SetTrackerActivity
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ExerciseInsightsFragment : Fragment(R.layout.fragment_exercise_insights), KodeinAware {

    override val kodein by kodein()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
        exerciseInstanceFactory
    }
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()
    private val trainingSetViewModel: TrainingSetViewModel by viewModels { trainingSetFactory }

    private var exerciseID: Int? = null

    private var maxWeightTrainingSet: TrainingSet? = null
    private var maxRepTrainingSet: TrainingSet? = null

    private lateinit var textViewTotalInstancesValue: TextView
    private lateinit var textViewTotalSetsValue: TextView
    private lateinit var textViewTotalRepsValue: TextView
    private lateinit var textViewMaxWeightValue: TextView
    private lateinit var textViewMaxRepsValue: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act: SetTrackerActivity = this.activity as SetTrackerActivity
        exerciseID = act.args.exerciseID

        textViewTotalInstancesValue = view.findViewById(R.id.text_view_total_instances_value)
        textViewTotalSetsValue = view.findViewById(R.id.text_view_total_sets_value)
        textViewTotalRepsValue = view.findViewById(R.id.text_view_total_reps_value)
        textViewMaxWeightValue = view.findViewById(R.id.text_view_max_weight_value)
        textViewMaxRepsValue = view.findViewById(R.id.text_view_max_reps_value)

        val exerciseInstancesObs =
            exerciseInstanceViewModel.getExerciseInstancesOfExercise(exerciseID)
        exerciseInstancesObs.observe(viewLifecycleOwner) {
            textViewTotalInstancesValue.text = it.size.toString()

            exerciseInstancesObs.removeObservers(viewLifecycleOwner)
        }

        val trainingSetsObs = trainingSetViewModel.getTrainingSetsOfExercise(exerciseID)
        trainingSetsObs.observe(viewLifecycleOwner) { trainingSets ->
            textViewTotalSetsValue.text = trainingSets.size.toString()

            var totalReps = 0
            var maxWeight = 0f
            var maxReps = 0
            for (trainingSet in trainingSets) {
                totalReps += trainingSet.reps
                if (trainingSet.weight > maxWeight) {
                    maxWeight = trainingSet.weight
                }
                if (trainingSet.reps > maxReps) {
                    maxReps = trainingSet.reps
                }
            }
            textViewTotalRepsValue.text = totalReps.toString()
            textViewMaxWeightValue.text = maxWeight.toString()
            textViewMaxRepsValue.text = maxReps.toString()

            trainingSetsObs.removeObservers(viewLifecycleOwner)
        }
    }
}