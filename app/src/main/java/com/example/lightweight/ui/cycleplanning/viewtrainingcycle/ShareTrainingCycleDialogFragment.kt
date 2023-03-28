package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShareTrainingCycleDialogFragment(
    private val trainingCycleDayAdapter: TrainingCycleDayAdapter,
    private val cycleID: Int?,
    private val cycleViewModel: CycleViewModel,
    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel,
    private val cycleDayExerciseViewModel: CycleDayExerciseViewModel
) : DialogFragment() {

    private val logTag = "ShareTrainingCycleDialogFragment"

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

        val adapter = ShareTrainingCycleAdapter(
            trainingCycleDayAdapter.items,
            trainingCycleDayAdapter.cycleDays,
            trainingCycleDayAdapter.idNamePairsCategory,
            trainingCycleDayAdapter.idNamePairsExercise
        )
        recyclerViewTrainingCycle.layoutManager = LinearLayoutManager(context)
        recyclerViewTrainingCycle.adapter = adapter

        buttonShareTrainingCycle.setOnClickListener {
            val checkedCycleDays = adapter.checkedCycleDays

            // Only allow the sharing to take place if at least one cycle day is selected
            if (checkedCycleDays.isEmpty()) {
                Toast.makeText(
                    context,
                    "Select at least one cycle day to share",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            var shareMessage = "Training cycle created on Lightweight\n"

            lifecycleScope.launch(Dispatchers.IO) {
                val cycleName = cycleViewModel.getCycleOfID(cycleID).cycleName
                shareMessage += "\n~~$cycleName~~\n"

                for (cycleDay in checkedCycleDays) {
                    // Add the name of the cycle day to the message
                    shareMessage += "\n~${cycleDay.cycleDayName}~\n"

                    val cycleDayCatCombos =
                        cycleDayCategoryViewModel.getCycleDayCatCombosOfCycle(cycleDay.cycleDayID)
                    for (cycleDayCatCombo in cycleDayCatCombos) {
                        // Add the category name to the message
                        shareMessage += "  ${cycleDayCatCombo.category_name}\n"

                        val cycleDayCatExCombos =
                            cycleDayExerciseViewModel.getCycleDayCatExCombosOfCycleDayCategory(
                                cycleDayCatCombo.cycle_day_category_ID
                            )
                        for (cycleDayCatExCombo in cycleDayCatExCombos) {
                            shareMessage += "      ${cycleDayCatExCombo.exercise_name}\n"
                        }
                    }
                }

                // Share the message to another app
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share to:"))

                Log.d(logTag, "Share message is \n$shareMessage")

                dismiss()
            }
        }

        buttonCancelShareTrainingCycle.setOnClickListener {
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