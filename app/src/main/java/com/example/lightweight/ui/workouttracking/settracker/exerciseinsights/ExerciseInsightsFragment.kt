package com.example.lightweight.ui.workouttracking.settracker.exerciseinsights

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.workouttracking.settracker.SetTrackerActivity
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModelFactory
import com.google.android.material.card.MaterialCardView
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ExerciseInsightsFragment : Fragment(R.layout.fragment_exercise_insights), KodeinAware {

    override val kodein by kodein()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
        exerciseInstanceFactory
    }
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()
    private val trainingSetViewModel: TrainingSetViewModel by viewModels { trainingSetFactory }

    private val adapter = PersonalRecordAdapter(listOf(), this)

    private var exerciseID: Int? = null

    private var maxWeightSet: TrainingSet? = null
    private var maxRepsSet: TrainingSet? = null
    private var prSets: ArrayList<TrainingSet> = ArrayList()

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
    private lateinit var cardViewPersonalRecords: MaterialCardView
    private lateinit var imageViewExpandMaxWeight: ImageView
    private lateinit var imageViewExpandMaxReps: ImageView
    private lateinit var imageViewExpandPersonalRecords: ImageView
    private lateinit var textViewMaxWeightSet: TextView
    private lateinit var textViewMaxRepsSet: TextView
    private lateinit var recyclerViewPersonalRecords: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act: SetTrackerActivity = this.activity as SetTrackerActivity
        exerciseID = act.args.exerciseID

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        getViewReferences(view)

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
                if (trainingSet.weight > maxWeight ||
                    (trainingSet.weight == maxWeight && maxWeightSet != null &&
                            trainingSet.reps > maxWeightSet?.reps!!)
                ) {
                    maxWeight = trainingSet.weight
                    maxWeightSet = trainingSet
                }
                if (trainingSet.reps > maxReps) {
                    maxReps = trainingSet.reps
                    maxRepsSet = trainingSet
                }
                if (trainingSet.isPR) {
                    prSets.add(trainingSet)
                }
            }
            textViewTotalRepsValue.text = totalReps.toString()
            textViewMaxWeightValue.text = maxWeight.toString()
            textViewMaxRepsValue.text = maxReps.toString()

            // Sort the training sets by their rep count in ascending order
            adapter.personalRecords = prSets.sortedWith(compareBy { it.reps })
            adapter.notifyDataSetChanged()

            if (maxWeightSet != null) {
                val dateObs =
                    exerciseInstanceViewModel.getExerciseInstanceDate(maxWeightSet!!.exerciseInstanceID)
                dateObs.observe(viewLifecycleOwner) { date ->
                    // Get the date formatted as (for example) 24 Jan 2023
                    val formattedDate = LocalDate.parse(date)
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

                    if (sharedPreferences.getString("unit", "kg") == "kg") {
                        if (maxWeightSet!!.reps == 1) {
                            // Display the set in kg using the singular 'rep'
                            textViewMaxWeightSet.text = resources.getString(
                                R.string.string_training_set_desc_kg_rep,
                                maxWeightSet!!.weight.toString(),
                                maxWeightSet!!.reps,
                                formattedDate
                            )
                        } else {
                            // Display the set in kg using the plural 'reps'
                            textViewMaxWeightSet.text = resources.getString(
                                R.string.string_training_set_desc_kg_reps,
                                maxWeightSet!!.weight.toString(),
                                maxWeightSet!!.reps,
                                formattedDate
                            )
                        }
                    } else {
                        if (maxWeightSet!!.reps == 1) {
                            // Display the set in lbs using the singular 'rep'
                            textViewMaxWeightSet.text = resources.getString(
                                R.string.string_training_set_desc_lbs_rep,
                                maxWeightSet!!.weight.toString(),
                                maxWeightSet!!.reps,
                                formattedDate
                            )
                        } else {
                            // Display the set in lbs using the plural 'reps'
                            textViewMaxWeightSet.text = resources.getString(
                                R.string.string_training_set_desc_lbs_reps,
                                maxWeightSet!!.weight.toString(),
                                maxWeightSet!!.reps,
                                formattedDate
                            )
                        }
                    }

                    dateObs.removeObservers(viewLifecycleOwner)
                }

            }
            if (maxRepsSet != null) {
                val dateObs =
                    exerciseInstanceViewModel.getExerciseInstanceDate(maxRepsSet!!.exerciseInstanceID)
                dateObs.observe(viewLifecycleOwner) { date ->
                    // Get the date formatted as (for example) 24 Jan 2023
                    val formattedDate = LocalDate.parse(date)
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

                    if (sharedPreferences.getString("unit", "kg") == "kg") {
                        if (maxRepsSet!!.reps == 1) {
                            // Display the set in kg using the singular 'rep'
                            textViewMaxRepsSet.text = resources.getString(
                                R.string.string_training_set_desc_kg_rep,
                                maxRepsSet!!.weight.toString(),
                                maxRepsSet!!.reps,
                                formattedDate
                            )
                        } else {
                            // Display the set in kg using the plural 'reps'
                            textViewMaxRepsSet.text = resources.getString(
                                R.string.string_training_set_desc_kg_reps,
                                maxRepsSet!!.weight.toString(),
                                maxRepsSet!!.reps,
                                formattedDate
                            )
                        }

                    } else {
                        if (maxRepsSet!!.reps == 1) {
                            // Display the set in lbs using the singular 'rep'
                            textViewMaxRepsSet.text = resources.getString(
                                R.string.string_training_set_desc_lbs_rep,
                                maxRepsSet!!.weight.toString(),
                                maxRepsSet!!.reps,
                                formattedDate
                            )
                        } else {
                            // Display the set in lbs using the plural 'reps'
                            textViewMaxRepsSet.text = resources.getString(
                                R.string.string_training_set_desc_lbs_reps,
                                maxRepsSet!!.weight.toString(),
                                maxRepsSet!!.reps,
                                formattedDate
                            )
                        }

                    }

                    dateObs.removeObservers(viewLifecycleOwner)
                }
            }

            trainingSetsObs.removeObservers(viewLifecycleOwner)
        }

        recyclerViewPersonalRecords.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPersonalRecords.adapter = adapter

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
                    View.VISIBLE
                }
                else -> {
                    imageViewExpandMaxReps.setImageResource(R.drawable.ic_baseline_expand_more_24)
                    View.GONE
                }
            }
        }

        cardViewPersonalRecords.setOnClickListener {
            recyclerViewPersonalRecords.visibility = when (recyclerViewPersonalRecords.visibility) {
                View.GONE -> {
                    imageViewExpandPersonalRecords.setImageResource(R.drawable.ic_baseline_expand_less_24)
                    View.VISIBLE
                }
                else -> {
                    imageViewExpandPersonalRecords.setImageResource(R.drawable.ic_baseline_expand_more_24)
                    View.GONE
                }
            }
        }
    }

    /**
     * Set up the views that will need to be referenced.
     */
    private fun getViewReferences(view: View) {
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
        cardViewPersonalRecords = view.findViewById(R.id.card_view_personal_records)
        imageViewExpandMaxWeight = view.findViewById(R.id.image_view_expand_max_weight)
        imageViewExpandMaxReps = view.findViewById(R.id.image_view_expand_max_reps)
        imageViewExpandPersonalRecords = view.findViewById(R.id.image_view_expand_personal_records)
        textViewMaxWeightSet = view.findViewById(R.id.text_view_max_weight_set)
        textViewMaxRepsSet = view.findViewById(R.id.text_view_max_reps_set)
        recyclerViewPersonalRecords = view.findViewById(R.id.recycler_view_personal_records)
    }
}