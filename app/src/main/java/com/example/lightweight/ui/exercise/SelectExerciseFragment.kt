package com.example.lightweight.ui.exercise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lightweight.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SelectExerciseFragment : Fragment(R.layout.fragment_select_exercise), KodeinAware {

    override val kodein by kodein()
    private val factory: ExerciseViewModelFactory by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}