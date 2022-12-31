package com.example.lightweight.ui.settracker.logsets

import android.opengl.Visibility
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.ui.category.ConfirmDeleteCategoryDialog
import com.example.lightweight.ui.category.EditCategoryDialog
import com.example.lightweight.ui.trainingset.TrainingSetViewModel

class TrainingSetItemAdapter(
    var trainingSets: List<TrainingSet>,
    private val trainingSetViewModel: TrainingSetViewModel,
) : RecyclerView.Adapter<TrainingSetItemAdapter.TrainingSetItemViewHolder>() {

    private lateinit var parent: ViewGroup

    private lateinit var imageViewTrophy: ImageView
    private lateinit var imageViewSetOptions: ImageView
    private lateinit var textViewSetNumber: TextView
    private lateinit var textViewSetWeight: TextView
    private lateinit var textViewSetReps: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingSetItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_set, parent, false)
        this.parent = parent
        return TrainingSetItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingSetItemViewHolder, position: Int) {
        val curTrainingSet = trainingSets[position]

        imageViewTrophy = holder.itemView.findViewById(R.id.image_view_trophy)
        imageViewSetOptions = holder.itemView.findViewById(R.id.image_view_training_set_options)
        textViewSetNumber = holder.itemView.findViewById(R.id.text_view_training_set_number)
        textViewSetWeight = holder.itemView.findViewById(R.id.text_view_training_set_weight)
        textViewSetReps = holder.itemView.findViewById(R.id.text_view_training_set_reps)

        textViewSetNumber.text = (position + 1).toString()
        textViewSetWeight.text = curTrainingSet.weight.toString()
        textViewSetReps.text = curTrainingSet.reps.toString()
        // Display the trophy if the set is a PR
        if (curTrainingSet.isPR) imageViewTrophy.visibility = View.VISIBLE
        else imageViewTrophy.visibility = View.INVISIBLE

        imageViewSetOptions.setOnClickListener {
            // Create the popup menu anchored to the training set item
            val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.END)

            popupMenu.inflate(R.menu.training_set_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_edit_training_set -> {
                        // Display the edit training set dialog

                        true
                    }
                    R.id.menu_item_delete_training_set -> {
                        // Delete the training set
                        trainingSetViewModel.delete(curTrainingSet)
                        Toast.makeText(parent.context, "Set has been deleted",
                            Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return trainingSets.size
    }

    inner class TrainingSetItemViewHolder(setView: View): RecyclerView.ViewHolder(setView)
}