package com.example.lightweight.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.WrapContentLinearLayoutManager
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class HomeParentWorkoutAdapter(
    var idNameMappings: Map<Int?, String>,
    private val fragment: HomeFragment
) : RecyclerView.Adapter<HomeParentWorkoutAdapter.HomeParentWorkoutViewHolder>(), KodeinAware {

    private val logTag = "HomeParentWorkoutAdapter"

    override val kodein by kodein(fragment.requireContext())
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by fragment.viewModels {
        exerciseInstanceFactory
    }
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()
    private val trainingSetViewModel: TrainingSetViewModel by fragment.viewModels { trainingSetFactory }

    private lateinit var parent: ViewGroup

    private lateinit var textViewExerciseName: TextView
    private lateinit var recyclerViewTrainingSets: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeParentWorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_exercise_instance, parent, false)

        this.parent = parent

        return HomeParentWorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeParentWorkoutViewHolder, position: Int) {
        val curName = idNameMappings.values.toTypedArray()[position]
        val curID = idNameMappings.keys.toTypedArray()[position]
        Log.d(logTag, "onBindViewHolder at position $position")

        textViewExerciseName = holder.itemView.findViewById(R.id.text_view_exercise_name)
        recyclerViewTrainingSets = holder.itemView.findViewById(R.id.recycler_view_training_sets)

        textViewExerciseName.text = curName

        // Set up the child recycler view
        val homeChildWorkoutAdapter = HomeChildWorkoutAdapter(listOf(), curID, fragment)
        recyclerViewTrainingSets.layoutManager = WrapContentLinearLayoutManager(
            holder.itemView.context, LinearLayoutManager.VERTICAL, false)
        recyclerViewTrainingSets.adapter = homeChildWorkoutAdapter

        trainingSetViewModel.getTrainingSetsOfExerciseInstance(curID)
            .observe(fragment.viewLifecycleOwner) {
                homeChildWorkoutAdapter.trainingSets = it
                homeChildWorkoutAdapter.notifyItemRangeChanged(0, it.size)
                Log.d(logTag, "Data set changed at position $position")
            }

        holder.itemView.setOnClickListener {
            navigateToExercise(curID)
        }
    }

    private fun navigateToExercise(exerciseInstanceID: Int?) {

        val exerciseObs = exerciseInstanceViewModel.getExerciseOfExerciseInstance(exerciseInstanceID)
        exerciseObs.observe(fragment.viewLifecycleOwner) {
            Log.d(logTag, "Exercise ID is $it")

            val action = HomeFragmentDirections.actionHomeFragmentToSetTrackerActivity(
                it!!, fragment.selectedDate.toString()
            )
            findNavController(fragment).navigate(action)

            exerciseObs.removeObservers(fragment.viewLifecycleOwner)
        }
    }

    override fun getItemCount(): Int {
        return idNameMappings.size
    }

    inner class HomeParentWorkoutViewHolder(setView: View): RecyclerView.ViewHolder(setView)
}