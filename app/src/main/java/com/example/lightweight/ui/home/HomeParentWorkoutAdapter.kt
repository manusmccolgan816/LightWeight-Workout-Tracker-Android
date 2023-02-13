package com.example.lightweight.ui.home

import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.IdNamePair
import com.example.lightweight.R
import com.example.lightweight.WrapContentLinearLayoutManager
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import com.example.lightweight.ui.workout.WorkoutViewModel
import com.example.lightweight.ui.workout.WorkoutViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class HomeParentWorkoutAdapter(
    private val recyclerViewPopulated: () -> Unit,
    var idNamePairs: List<IdNamePair>, // A list of exercise instance IDs and their exercise name
    private val fragment: HomeFragment
) : RecyclerView.Adapter<HomeParentWorkoutAdapter.HomeParentWorkoutViewHolder>(), KodeinAware {

    private val logTag = "HomeParentWorkoutAdapter"

    override val kodein by kodein(fragment.requireContext())
    private val workoutFactory: WorkoutViewModelFactory by instance()
    private val workoutViewModel: WorkoutViewModel by fragment.viewModels { workoutFactory }
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by fragment.viewModels {
        exerciseInstanceFactory
    }
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()
    private val trainingSetViewModel: TrainingSetViewModel by fragment.viewModels { trainingSetFactory }

    private lateinit var parent: ViewGroup

    private lateinit var textViewExerciseName: TextView
    private lateinit var imageViewExerciseInstanceOptions: ImageView
    private lateinit var recyclerViewTrainingSets: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeParentWorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_exercise_instance, parent, false)

        this.parent = parent

        return HomeParentWorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeParentWorkoutViewHolder, position: Int) {
        val curID = idNamePairs[position].id
        val curName = idNamePairs[position].name
        Log.d(logTag, "onBindViewHolder at position $position")

        textViewExerciseName = holder.itemView.findViewById(R.id.text_view_exercise_name)
        imageViewExerciseInstanceOptions =
            holder.itemView.findViewById(R.id.image_view_exercise_instance_options)
        recyclerViewTrainingSets = holder.itemView.findViewById(R.id.recycler_view_training_sets)

        textViewExerciseName.text = curName

        // Set up the child recycler view
        val homeChildWorkoutAdapter = HomeChildWorkoutAdapter(
            recyclerViewPopulated,
            listOf(),
            curID,
            fragment
        )
        recyclerViewTrainingSets.layoutManager = WrapContentLinearLayoutManager(
            holder.itemView.context, LinearLayoutManager.VERTICAL, false
        )
        recyclerViewTrainingSets.adapter = homeChildWorkoutAdapter

        trainingSetViewModel.getTrainingSetsOfExerciseInstance(curID)
            .observe(fragment.viewLifecycleOwner) {
                homeChildWorkoutAdapter.trainingSets = it
                homeChildWorkoutAdapter.notifyItemRangeChanged(0, it.size)
                Log.d(logTag, "Data set changed at position $position")
            }

        imageViewExerciseInstanceOptions.setOnClickListener {
            val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.END)

            popupMenu.inflate(R.menu.exercise_instance_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_delete_exercise_instance -> {
                        val getEiObserver =
                            exerciseInstanceViewModel.getExerciseInstanceOfIDObs(curID)
                        getEiObserver.observe(fragment.viewLifecycleOwner) { exerciseInstance ->
                            // If this is the only exercise instance in the workout, the workout
                            // will be deleted
                            val deleteWorkout: Boolean = idNamePairs.size == 1

                            // Delete the exercise instance
                            exerciseInstanceViewModel.delete(exerciseInstance)

                            if (deleteWorkout) {
                                workoutViewModel.deleteWorkoutOfID(fragment.workoutID)
                            }

                            // Decrement the exercise instance number of all exercise instances in
                            // the workout with a higher exercise instance number to preserve ordering
                            exerciseInstanceViewModel.decrementExerciseInstanceNumbersOfWorkoutAfter(
                                exerciseInstance.workoutID,
                                exerciseInstance.exerciseInstanceNumber
                            )

                            getEiObserver.removeObservers(fragment.viewLifecycleOwner)
                        }

                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }

        holder.itemView.setOnClickListener {
            navigateToExercise(curID)
        }
    }

    private fun navigateToExercise(exerciseInstanceID: Int?) {
        val exerciseObs =
            exerciseInstanceViewModel.getExerciseOfExerciseInstance(exerciseInstanceID)
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
        return idNamePairs.size
    }

    inner class HomeParentWorkoutViewHolder(setView: View) : RecyclerView.ViewHolder(setView)
}