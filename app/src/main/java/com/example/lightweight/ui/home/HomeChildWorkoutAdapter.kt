package com.example.lightweight.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class HomeChildWorkoutAdapter(
    private val recyclerViewPopulated: () -> Unit,
    var trainingSets: List<TrainingSet>,
    val exerciseInstanceID: Int?,
    private val fragment: HomeFragment
) : RecyclerView.Adapter<HomeChildWorkoutAdapter.HomeChildWorkoutViewHolder>(), KodeinAware {

    private val logTag = "HomeChildWorkoutAdapter"

    override val kodein by kodein(fragment.requireContext())
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by fragment.viewModels {
        exerciseInstanceFactory
    }

    private lateinit var parent: ViewGroup

    private lateinit var imageViewTrophy: ImageView
    private lateinit var imageViewSetNote: ImageView
    private lateinit var textViewSetWeight: TextView
    private lateinit var textViewSetReps: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : HomeChildWorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_training_set, parent, false)
        this.parent = parent
        return HomeChildWorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeChildWorkoutViewHolder, position: Int) {
        val curTrainingSet = trainingSets[position]

        imageViewTrophy = holder.itemView.findViewById(R.id.image_view_trophy)
        imageViewSetNote = holder.itemView.findViewById(R.id.image_view_training_set_note)
        textViewSetWeight = holder.itemView.findViewById(R.id.text_view_training_set_weight)
        textViewSetReps = holder.itemView.findViewById(R.id.text_view_training_set_reps)

        textViewSetWeight.text = curTrainingSet.weight.toString() + "kg"
        if (curTrainingSet.reps == 1) {
            textViewSetReps.text = curTrainingSet.reps.toString() + " rep"
        } else {
            textViewSetReps.text = curTrainingSet.reps.toString() + " reps"
        }

        // If the training set has a note...
        if (!curTrainingSet.note.isNullOrBlank()) {
            // ...display the text bubble icon
            imageViewSetNote.visibility = View.VISIBLE
        }

        // Display the trophy if the set is a PR
        if (curTrainingSet.isPR) imageViewTrophy.visibility = View.VISIBLE
        else imageViewTrophy.visibility = View.INVISIBLE

        holder.itemView.setOnClickListener {
            navigateToExercise(exerciseInstanceID)
        }

        // If the recycler view has been populated
        if (position == itemCount - 1) {
            // Call the function to display the recycler view
            recyclerViewPopulated()
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
            NavHostFragment.findNavController(fragment).navigate(action)

            exerciseObs.removeObservers(fragment.viewLifecycleOwner)
        }
    }

    override fun getItemCount(): Int {
        return trainingSets.size
    }

    inner class HomeChildWorkoutViewHolder(setView: View) : RecyclerView.ViewHolder(setView)
}