package com.example.lightweight.ui.settracker.exerciseinsights

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.settracker.SetTrackerActivity
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import com.google.android.material.card.MaterialCardView
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

    private var maxWeightSet: TrainingSet? = null
    private var maxRepsSet: TrainingSet? = null

    private lateinit var layout: ConstraintLayout
    private lateinit var constraintLayoutMaxWeight: ConstraintLayout
    private lateinit var constraintLayoutMaxReps: ConstraintLayout
    private lateinit var textViewTotalInstancesValue: TextView
    private lateinit var textViewTotalSetsValue: TextView
    private lateinit var textViewTotalRepsValue: TextView
    private lateinit var textViewMaxWeightValue: TextView
    private lateinit var textViewMaxRepsValue: TextView
    private lateinit var cardViewMaxWeight: MaterialCardView
    private lateinit var cardViewMaxReps: MaterialCardView
    private lateinit var imageViewExpandMaxWeight: ImageView
    private lateinit var imageViewExpandMaxReps: ImageView
    private lateinit var textViewMaxWeightSet: TextView
    private lateinit var textViewMaxRepsSet: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act: SetTrackerActivity = this.activity as SetTrackerActivity
        exerciseID = act.args.exerciseID

        layout = view.findViewById(R.id.constraint_layout_exercise_insights)
        constraintLayoutMaxWeight = view.findViewById(R.id.constraint_layout_max_weight)
        constraintLayoutMaxReps = view.findViewById(R.id.constraint_layout_max_reps)
        textViewTotalInstancesValue = view.findViewById(R.id.text_view_total_instances_value)
        textViewTotalSetsValue = view.findViewById(R.id.text_view_total_sets_value)
        textViewTotalRepsValue = view.findViewById(R.id.text_view_total_reps_value)
        textViewMaxWeightValue = view.findViewById(R.id.text_view_max_weight_value)
        textViewMaxRepsValue = view.findViewById(R.id.text_view_max_reps_value)
        cardViewMaxWeight = view.findViewById(R.id.card_view_max_weight)
        cardViewMaxReps = view.findViewById(R.id.card_view_max_reps)
        imageViewExpandMaxWeight = view.findViewById(R.id.image_view_expand_max_weight)
        imageViewExpandMaxReps = view.findViewById(R.id.image_view_expand_max_reps)
        textViewMaxWeightSet = view.findViewById(R.id.text_view_max_weight_set)
        textViewMaxRepsSet = view.findViewById(R.id.text_view_max_reps_set)

        // Provides smooth animation which is clear when expanding and minimising views
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

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
                if (trainingSet.weight >= maxWeight) {
                    maxWeight = trainingSet.weight
                    if (maxRepsSet == null || trainingSet.reps > maxWeightSet?.reps!!) {
                        maxWeightSet = trainingSet
                    }
                }
                if (trainingSet.reps > maxReps) {
                    maxReps = trainingSet.reps
                    maxRepsSet = trainingSet
                }
            }
            textViewTotalRepsValue.text = totalReps.toString()
            textViewMaxWeightValue.text = maxWeight.toString()
            textViewMaxRepsValue.text = maxReps.toString()

            if (maxWeightSet != null) {
                val dateObs =
                    exerciseInstanceViewModel.getExerciseInstanceDate(maxWeightSet!!.exerciseInstanceID)
                dateObs.observe(viewLifecycleOwner) { date ->
                    textViewMaxWeightSet.text = resources.getString(
                        R.string.string_training_set_desc,
                        maxWeightSet!!.weight.toString(),
                        maxWeightSet!!.reps,
                        date
                    )
                    dateObs.removeObservers(viewLifecycleOwner)
                }

            }
            if (maxRepsSet != null) {
                val dateObs =
                    exerciseInstanceViewModel.getExerciseInstanceDate(maxWeightSet!!.exerciseInstanceID)
                dateObs.observe(viewLifecycleOwner) { date ->
                    textViewMaxRepsSet.text = resources.getString(
                        R.string.string_training_set_desc,
                        maxRepsSet!!.weight.toString(),
                        maxRepsSet!!.reps,
                        date
                    )
                    dateObs.removeObservers(viewLifecycleOwner)
                }

            }

            trainingSetsObs.removeObservers(viewLifecycleOwner)
        }

        cardViewMaxWeight.setOnClickListener {
            textViewMaxWeightSet.visibility = when (textViewMaxWeightSet.visibility) {
                View.GONE -> {
                    imageViewExpandMaxWeight.setImageResource(R.drawable.ic_baseline_expand_less_24)
                    View.VISIBLE // If the view is not visible, make it visible
                }
                else -> {
                    imageViewExpandMaxWeight.setImageResource(R.drawable.ic_baseline_expand_more_24)
                    View.GONE // If the view is visible, hide it
                }
            }
        }

        cardViewMaxReps.setOnClickListener {
            textViewMaxRepsSet.visibility = when (textViewMaxRepsSet.visibility) {
                View.GONE -> {
                    imageViewExpandMaxReps.setImageResource(R.drawable.ic_baseline_expand_less_24)
                    View.VISIBLE // If the view is not visible, make it visible
                }
                else -> {
                    imageViewExpandMaxReps.setImageResource(R.drawable.ic_baseline_expand_more_24)
                    View.GONE // If the view is visible, hide it
                }
            }
        }
    }
}