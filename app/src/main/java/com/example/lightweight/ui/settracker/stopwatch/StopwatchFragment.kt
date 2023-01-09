package com.example.lightweight.ui.settracker.stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import com.example.lightweight.R

class StopwatchFragment : Fragment(R.layout.fragment_stopwatch) {

    private var running = false
    private var pauseOffset: Long = 0

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
            startChronometer()
        }
        buttonPause.setOnClickListener {
            pauseChronometer()
        }
        buttonReset.setOnClickListener {
            resetChronometer()
        }

        if (savedInstanceState != null) {
            chronometer.restoreInstanceState(savedInstanceState)
//            running = savedInstanceState.getBoolean("running")
//            pauseOffset = savedInstanceState.getLong("pauseOffset")
        }
    }

    private fun startChronometer() {
        if (!running) {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            running = true
        }
    }

    private fun pauseChronometer() {
        if (running) {
            chronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
            running = false
        }
    }

    private fun resetChronometer() {
        chronometer.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        chronometer.saveInstanceState(outState)
//        outState.putBoolean("running", running)
//        outState.putLong("pauseOffset", pauseOffset)
    }
}