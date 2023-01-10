package com.example.lightweight.ui.settracker.stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import com.example.lightweight.R

class StopwatchFragment : Fragment(R.layout.fragment_stopwatch) {

    private lateinit var chronometer: ChronometerWithPause
    private lateinit var buttonStart: Button
    private lateinit var buttonPause: Button
    private lateinit var buttonReset: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chronometer = view.findViewById(R.id.chronometer)
        buttonStart = view.findViewById(R.id.button_start)
        buttonPause = view.findViewById(R.id.button_pause)
        buttonReset = view.findViewById(R.id.button_reset)

        buttonStart.setOnClickListener {
            if (!chronometer.isRunning)
                chronometer.start()
        }
        buttonPause.setOnClickListener {
            if (chronometer.isRunning)
                chronometer.stop()
        }
        buttonReset.setOnClickListener {
            chronometer.reset()
        }

        if (savedInstanceState != null) {
            chronometer.restoreInstanceState(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        chronometer.saveInstanceState(outState)
    }
}