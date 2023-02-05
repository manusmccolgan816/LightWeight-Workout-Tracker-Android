package com.example.lightweight.ui.settracker.exercisestats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lightweight.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class ExerciseInsightsFragment : Fragment(R.layout.fragment_exercise_insights), KodeinAware {

    override val kodein by kodein()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}