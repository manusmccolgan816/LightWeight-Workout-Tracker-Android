package com.example.lightweight.ui.exercise

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.ui.category.SelectCategoryFragmentDirections

class ExerciseItemAdapter(
    var exercises: List<Exercise>,
    private val viewModel: ExerciseViewModel,
    var fragment: Fragment
) : RecyclerView.Adapter<ExerciseItemAdapter.ExerciseItemViewHolder>() {

    private lateinit var parent: ViewGroup
    private lateinit var imageViewExerciseOptions: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ExerciseItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        this.parent = parent
        return ExerciseItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseItemViewHolder, position: Int) {
        val curExercise = exercises[position]

        holder.itemView.findViewById<TextView>(R.id.text_view_exercise_name)
            .text = curExercise.exerciseName

        imageViewExerciseOptions = holder.itemView.findViewById(R.id.image_view_exercise_options)
        // Set up popup menu for each exercise when icon is clicked
        imageViewExerciseOptions.setOnClickListener {
            // Create the popup menu anchored to the category item
            val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.END)

            popupMenu.inflate(R.menu.exercise_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_edit_exercise -> {
                        // Display the edit exercise dialog
                        EditExerciseDialog(parent.context, curExercise,
                            fun (exerciseID: Int?, newName: String) {
                                viewModel.updateName(exerciseID, newName)
                            }).show()
                        true
                    }
                    R.id.menu_item_delete_exercise -> {
                        ConfirmDeleteExerciseDialog(parent.context, curExercise,
                            fun (curExercise: Exercise) { viewModel.delete(curExercise) }).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }

        // Navigate to SelectExerciseFragment when a category item is selected, passing the category
        // as a parameter
        holder.itemView.setOnClickListener {

            val action = SelectExerciseFragmentDirections
                .actionSelectExerciseFragmentToSetTrackerActivity(curExercise.exerciseID!!)
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    inner class ExerciseItemViewHolder(exerciseView: View): RecyclerView.ViewHolder(exerciseView)
}