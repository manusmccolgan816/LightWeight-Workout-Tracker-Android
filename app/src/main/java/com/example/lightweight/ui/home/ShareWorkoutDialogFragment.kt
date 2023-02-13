package com.example.lightweight.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.IdNamePair
import com.example.lightweight.R

class ShareWorkoutDialogFragment(
    private var idNamePairs: List<IdNamePair>, // A list of exercise instance IDs and their exercise name
    private val fragment: Fragment
) : DialogFragment() {

    private lateinit var recyclerViewExerciseInstances: RecyclerView
    private lateinit var buttonShareWorkout: Button
    private lateinit var buttonCancelShareWorkout: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_share_workout, container, false)

        recyclerViewExerciseInstances = view.findViewById(R.id.recycler_view_share_exercise_instances)!!
        buttonShareWorkout = view.findViewById(R.id.button_share_workout)!!
        buttonCancelShareWorkout = view.findViewById(R.id.button_cancel_share_workout)!!

        val adapter =  ShareWorkoutAdapter(idNamePairs, fragment)
        recyclerViewExerciseInstances.layoutManager = LinearLayoutManager(context)
        recyclerViewExerciseInstances.adapter = adapter

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