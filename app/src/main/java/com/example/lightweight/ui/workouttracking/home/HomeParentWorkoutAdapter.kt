package com.example.lightweight.ui.workouttracking.home

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.IdNamePair
import com.example.lightweight.R
import com.example.lightweight.WrapContentLinearLayoutManager
import com.example.lightweight.util.PersonalRecordUtil

class HomeParentWorkoutAdapter(
    private val recyclerViewPopulated: () -> Unit,
    var idNamePairs: List<IdNamePair>, // A list of exercise instance IDs and their exercise name
    private val fragment: HomeFragment,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<HomeParentWorkoutAdapter.HomeParentWorkoutViewHolder>() {

    private val logTag = "HomeParentWorkoutAdapter"

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
            fragment,
            viewModel
        )
        recyclerViewTrainingSets.layoutManager = WrapContentLinearLayoutManager(
            holder.itemView.context, LinearLayoutManager.VERTICAL, false
        )
        recyclerViewTrainingSets.adapter = homeChildWorkoutAdapter

        viewModel.getTrainingSetsOfExerciseInstance(curID)
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
                            viewModel.getExerciseInstanceOfIDObs(curID)
                        getEiObserver.observe(fragment.viewLifecycleOwner) { exerciseInstance ->
                            deleteTrainingSets(exerciseInstance.exerciseInstanceID)

                            // If this is the only exercise instance in the workout, the workout
                            // will be deleted
                            val deleteWorkout: Boolean = idNamePairs.size == 1

                            // Delete the exercise instance
                            viewModel.deleteExerciseInstance(exerciseInstance)

                            if (deleteWorkout) {
                                viewModel.deleteWorkoutOfID(fragment.workoutID)
                            }

                            // Decrement the exercise instance number of all exercise instances in
                            // the workout with a higher exercise instance number to preserve ordering
                            viewModel.decrementExerciseInstanceNumbersOfWorkoutAfter(
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

    override fun getItemCount(): Int {
        return idNamePairs.size
    }

    private fun navigateToExercise(exerciseInstanceID: Int?) {
        val exerciseObs =
            viewModel.getExerciseOfExerciseInstance(exerciseInstanceID)
        exerciseObs.observe(fragment.viewLifecycleOwner) {
            Log.d(logTag, "Exercise ID is $it")

            val action = HomeFragmentDirections.actionHomeFragmentToSetTrackerActivity(
                it!!, fragment.selectedDate.toString()
            )

            findNavController(fragment).navigate(action)

            exerciseObs.removeObservers(fragment.viewLifecycleOwner)
        }
    }

    private fun deleteTrainingSets(exerciseInstanceID: Int?) {
        val trainingSetsObs =
            viewModel.getTrainingSetsOfExerciseInstance(exerciseInstanceID)
        trainingSetsObs.observe(fragment.viewLifecycleOwner) { trainingSets ->
            trainingSetsObs.removeObservers(fragment.viewLifecycleOwner)
            val exerciseIDObs =
                viewModel.getExerciseOfExerciseInstance(exerciseInstanceID)
            exerciseIDObs.observe(fragment.viewLifecycleOwner) { exerciseID ->
                exerciseIDObs.removeObservers(fragment.viewLifecycleOwner)

                for (curTrainingSet in trainingSets) {
                    // If the set to be deleted is a PR, another set may become a PR
                    if (curTrainingSet.isPR) {
                        val prTrainingSetsObs = viewModel
                            .getTrainingSetsOfExerciseAndIsPR(exerciseID, 1)
                        prTrainingSetsObs.observe(fragment.viewLifecycleOwner) { prSets ->
                            prTrainingSetsObs.removeObservers(fragment.viewLifecycleOwner)

                            val sameRepSetsObs = viewModel
                                .getTrainingSetsOfExerciseRepsIsPR(
                                    exerciseID,
                                    curTrainingSet.reps, 0
                                )
                            sameRepSetsObs.observe(fragment.viewLifecycleOwner) { sameRepSets ->
                                sameRepSetsObs.removeObservers(fragment.viewLifecycleOwner)

                                val lowerRepSetsObs =
                                    viewModel.getTrainingSetsOfExerciseFewerReps(
                                        exerciseID, curTrainingSet.reps
                                    )
                                lowerRepSetsObs.observe(fragment.viewLifecycleOwner) { lowerRepSets ->
                                    lowerRepSetsObs.removeObservers(fragment.viewLifecycleOwner)

                                    val newPrIds = PersonalRecordUtil.getNewPrSetsOnDeletion(
                                        curTrainingSet,
                                        prSets,
                                        sameRepSets,
                                        lowerRepSets
                                    )

                                    // Set isPr to true for all training sets that are now PRs
                                    for (id in newPrIds) {
                                        viewModel.updateIsPR(id, 1)
                                    }
                                }
                            }
                        }
                    }
                    if (this.itemCount > 1) {
                        // Decrement trainingSetNumber of all sets after curTrainingSet
                        viewModel.decrementTrainingSetNumbersAbove(
                            curTrainingSet.exerciseInstanceID,
                            curTrainingSet.trainingSetNumber
                        )

                        // Delete the training set
                        viewModel.deleteTrainingSet(curTrainingSet)
                    }
                }
            }
        }
    }

    inner class HomeParentWorkoutViewHolder(setView: View) : RecyclerView.ViewHolder(setView)
}