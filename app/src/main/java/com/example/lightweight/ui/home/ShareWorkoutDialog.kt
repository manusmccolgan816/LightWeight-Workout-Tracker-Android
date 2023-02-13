package com.example.lightweight.ui.home

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.IdNamePair
import com.example.lightweight.R

class ShareWorkoutDialog(
    context: Context,
    private var idNamePairs: List<IdNamePair>, // A list of exercise instance IDs and their exercise name
) : AppCompatDialog(context) {

    private lateinit var recyclerViewExerciseInstances: RecyclerView
    private lateinit var buttonShareWorkout: Button
    private lateinit var buttonCancelShareWorkout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_share_workout)

        recyclerViewExerciseInstances = findViewById(R.id.recycler_view_share_exercise_instances)!!
        buttonShareWorkout = findViewById(R.id.button_share_workout)!!
        buttonCancelShareWorkout = findViewById(R.id.button_cancel_share_workout)!!

        val adapter =  ShareWorkoutAdapter(idNamePairs)
        recyclerViewExerciseInstances.layoutManager = LinearLayoutManager(context)
        recyclerViewExerciseInstances.adapter = adapter

        buttonCancelShareWorkout.setOnClickListener {
            cancel()
        }
    }
}