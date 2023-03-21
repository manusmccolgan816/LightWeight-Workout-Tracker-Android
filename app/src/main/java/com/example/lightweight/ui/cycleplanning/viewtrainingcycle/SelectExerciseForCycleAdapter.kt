package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.ui.workouttracking.selectexercise.ConfirmDeleteExerciseDialog
import com.example.lightweight.ui.workouttracking.selectexercise.EditExerciseDialog
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SelectExerciseForCycleAdapter(
    var exercises: List<Exercise>,
    val selectExercise: (Exercise) -> Unit,
    val fragment: Fragment
) : RecyclerView.Adapter<SelectExerciseForCycleAdapter.SelectExerciseForCycleViewHolder>(),
    KodeinAware {

    override val kodein by kodein(fragment.requireContext())
    private val exerciseFactory: ExerciseViewModelFactory by instance()
    private val exerciseViewModel: ExerciseViewModel by fragment.viewModels { exerciseFactory }

    private lateinit var parent: ViewGroup

    private lateinit var textViewExerciseName: TextView
    private lateinit var imageViewExerciseOptions: ImageView

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectExerciseForCycleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        this.parent = parent
        return SelectExerciseForCycleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectExerciseForCycleViewHolder, position: Int) {
        val curExercise = exercises[position]

        textViewExerciseName = holder.itemView.findViewById(R.id.text_view_exercise_name)
        imageViewExerciseOptions = holder.itemView.findViewById(R.id.image_view_exercise_options)

        textViewExerciseName.text = curExercise.exerciseName
        textViewExerciseName.setOnClickListener {
            selectExercise(curExercise)
        }

        imageViewExerciseOptions.setOnClickListener {
            val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.END)

            popupMenu.inflate(R.menu.exercise_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_edit_exercise -> {
                        EditExerciseDialog(
                            parent.context,
                            curExercise,
                            fun(exerciseID: Int?, newName: String) {
                                exerciseViewModel.updateName(exerciseID, newName)
                            }
                        ).show()
                        true
                    }
                    R.id.menu_item_delete_exercise -> {
                        ConfirmDeleteExerciseDialog(
                            parent.context,
                            curExercise,
                            fun(curExercise: Exercise) {
                                exerciseViewModel.delete(curExercise)
                            }
                        ).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    inner class SelectExerciseForCycleViewHolder(view: View) : RecyclerView.ViewHolder(view)
}