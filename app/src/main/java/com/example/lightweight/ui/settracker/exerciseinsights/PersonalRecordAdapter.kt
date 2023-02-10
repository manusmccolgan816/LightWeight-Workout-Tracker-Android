package com.example.lightweight.ui.settracker.exerciseinsights

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet

class PersonalRecordAdapter(
    var personalRecords: List<TrainingSet>,
    private val fragment: Fragment
) : RecyclerView.Adapter<PersonalRecordAdapter.PersonalRecordViewHolder>() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var textViewPersonalRecordReps: TextView
    private lateinit var textViewPersonalRecordWeight: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalRecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_personal_record, parent, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.requireContext())

        return PersonalRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonalRecordViewHolder, position: Int) {
        val curPersonalRecord = personalRecords[position]

        textViewPersonalRecordReps =
            holder.itemView.findViewById(R.id.text_view_personal_record_reps)
        textViewPersonalRecordWeight =
            holder.itemView.findViewById(R.id.text_view_personal_record_weight)

        textViewPersonalRecordReps.text = fragment.resources.getString(
            R.string.string_pr_rep_desc,
            curPersonalRecord.reps
        )


        if (sharedPreferences.getString("unit", "kg") == "kg") {
            textViewPersonalRecordWeight.text = fragment.resources.getString(
                R.string.string_pr_weight_desc_kg,
                curPersonalRecord.weight.toString()
            )
        }
        else {
            textViewPersonalRecordWeight.text = fragment.resources.getString(
                R.string.string_pr_weight_desc_lbs,
                curPersonalRecord.weight.toString()
            )
        }
    }

    override fun getItemCount(): Int {
        return personalRecords.size
    }

    inner class PersonalRecordViewHolder(view: View) : RecyclerView.ViewHolder(view)
}