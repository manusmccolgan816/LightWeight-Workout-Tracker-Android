package com.example.lightweight.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.IdNamePair
import com.example.lightweight.R
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ShareWorkoutDialogFragment(
    private val selectedDate: LocalDate,
    private var idNamePairs: List<IdNamePair>, // A list of exercise instance IDs and their exercise name
    private val fragment: Fragment
) : DialogFragment(), KodeinAware {

    private val logTag = "ShareWorkoutDialogFragment"

    override val kodein by kodein(fragment.requireContext())
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()
    private val trainingSetViewModel: TrainingSetViewModel by fragment.viewModels {
        trainingSetFactory
    }

    private lateinit var recyclerViewExerciseInstances: RecyclerView
    private lateinit var buttonShareWorkout: Button
    private lateinit var buttonCancelShareWorkout: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_share_workout, container, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        recyclerViewExerciseInstances =
            view.findViewById(R.id.recycler_view_share_exercise_instances)!!
        buttonShareWorkout = view.findViewById(R.id.button_share_workout)!!
        buttonCancelShareWorkout = view.findViewById(R.id.button_cancel_share_workout)!!

        val adapter = ShareWorkoutParentAdapter(idNamePairs, fragment)
        recyclerViewExerciseInstances.layoutManager = LinearLayoutManager(context)
        recyclerViewExerciseInstances.adapter = adapter

        buttonShareWorkout.setOnClickListener {
            val checkedIDNamePairs = adapter.checkedIDNamePairs

            // Only share the workout if at least one exercise instance has been selected
            if (checkedIDNamePairs.isEmpty()) {
                Toast.makeText(context, "Select at least one exercise to share", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
            var shareMessage = "Lightweight workout on ${selectedDate.format(formatter)}\n"

            lifecycleScope.launch(Dispatchers.IO) {
                for (idName in checkedIDNamePairs) {
                    // Add the exercise name to the message
                    shareMessage += "\n~${idName.name}~\n"
                    val trainingSets =
                        trainingSetViewModel.getTrainingSetsOfExerciseInstanceNoLiveData(idName.id)

                    for (trainingSet in trainingSets) {
                        // Display 'rep' or 'reps' after the number of reps
                        val repText = when (trainingSet.reps) {
                            1 -> {
                                resources.getString(
                                    R.string.string_number_rep,
                                    trainingSet.reps
                                )
                            }
                            else -> {
                                resources.getString(
                                    R.string.string_number_reps,
                                    trainingSet.reps
                                )
                            }
                        }

                        // Display the weight with the right unit
                        val weightText = when (sharedPreferences.getString("unit", "kg")) {
                            "kg" -> {
                                resources.getString(
                                    R.string.string_weight_kg,
                                    trainingSet.weight.toString()
                                )
                            }
                            else -> {
                                resources.getString(
                                    R.string.string_weight_lbs,
                                    trainingSet.weight.toString()
                                )
                            }
                        }

                        // Add the training set to the message
                        shareMessage += "$weightText x $repText\n"
                    }
                }

                // Share the message to another app
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share to:"))

                Log.d(logTag, "Share message is \n$shareMessage")

                dismiss()
            }
        }

        buttonCancelShareWorkout.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        // Set the width and height as XML values do not work
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}