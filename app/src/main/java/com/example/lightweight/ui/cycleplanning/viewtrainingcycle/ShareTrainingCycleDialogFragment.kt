package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R

class ShareTrainingCycleDialogFragment(

) : DialogFragment() {

    private lateinit var recyclerViewTrainingCycle: RecyclerView
    private lateinit var buttonShareTrainingCycle: Button
    private lateinit var buttonCancelShareTrainingCycle: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_share_training_cycle, container, false)

        recyclerViewTrainingCycle = view.findViewById(R.id.recycler_view_share_training_cycle)
        buttonShareTrainingCycle = view.findViewById(R.id.button_share_training_cycle)
        buttonCancelShareTrainingCycle = view.findViewById(R.id.button_cancel_share_training_cycle)



        return super.onCreateView(inflater, container, savedInstanceState)
    }
}