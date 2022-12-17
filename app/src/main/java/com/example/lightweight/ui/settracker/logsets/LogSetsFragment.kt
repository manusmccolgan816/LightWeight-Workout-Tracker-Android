package com.example.lightweight.ui.settracker.logsets

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lightweight.R

class LogSetsFragment : Fragment(R.layout.fragment_log_sets) {

    private lateinit var editTextWeight: EditText
    private lateinit var editTextNumReps: EditText
    private lateinit var buttonClearSet: Button
    private lateinit var buttonSaveSet: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextWeight = view.findViewById(R.id.edit_text_weight)
        editTextNumReps = view.findViewById(R.id.edit_text_num_reps)
        buttonClearSet = view.findViewById(R.id.button_clear_set)
        buttonClearSet.setOnClickListener {
            editTextWeight.text.clear()
            editTextNumReps.text.clear()
            Toast.makeText(requireContext(), "Text cleared", Toast.LENGTH_SHORT).show()
        }
        buttonSaveSet = view.findViewById(R.id.button_save_set)
    }
}