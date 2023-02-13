package com.example.lightweight.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.IdNamePair
import com.example.lightweight.R

class ShareWorkoutAdapter(
    var idNamePairs: List<IdNamePair>, // A list of exercise instance IDs and their exercise name
) : RecyclerView.Adapter<ShareWorkoutAdapter.ShareWorkoutViewHolder>() {

    private lateinit var checkBoxExerciseInstance: AppCompatCheckBox

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareWorkoutViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_share_workout, parent, false)

        return ShareWorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShareWorkoutViewHolder, position: Int) {
        val curID = idNamePairs[position].id
        val curName = idNamePairs[position].name

        checkBoxExerciseInstance = holder.itemView.findViewById(R.id.check_box_exercise_instance)

        checkBoxExerciseInstance.text = curName
    }

    override fun getItemCount(): Int {
        return idNamePairs.size
    }

    inner class ShareWorkoutViewHolder(view: View) : RecyclerView.ViewHolder(view)
}