package com.example.lightweight.ui.settracker.stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import com.example.lightweight.R
import com.example.lightweight.SessionManager
import java.text.SimpleDateFormat

class StopwatchFragment : Fragment(R.layout.fragment_stopwatch) {

    private val logTag = "StopwatchFragment"

    private lateinit var sessionManager: SessionManager
    private lateinit var format: SimpleDateFormat
    private lateinit var currentTime: String
    private lateinit var chronometer: Chronometer
    private var pauseOffset: Long = 0
    //private var running = false

    //private lateinit var chronometer: ChronometerWithPause
    private lateinit var buttonStart: Button
    private lateinit var buttonPause: Button
    private lateinit var buttonReset: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chronometer = view.findViewById(R.id.chronometer)
        buttonStart = view.findViewById(R.id.button_start)
        buttonPause = view.findViewById(R.id.button_pause)
        buttonReset = view.findViewById(R.id.button_reset)

        sessionManager = SessionManager(this.activity?.applicationContext)

        val isRunningBackground = sessionManager.stopwatchRunningBackground
        Log.d(logTag, "isRunningBackground: $isRunningBackground")
        Log.d(logTag, "stopwatchRunning: ${sessionManager.stopwatchRunning}")
        if (isRunningBackground) {
            val smCurrentTime: Long = sessionManager.currentTime
            if (sessionManager.stopwatchRunning) {
                val currentTime: Long = SystemClock.elapsedRealtime()
                val mils: Long = currentTime - smCurrentTime
                chronometer.base = SystemClock.elapsedRealtime() - mils
                chronometer.start()

                Log.d(logTag, "smCurrentTime: $smCurrentTime")
                Log.d(logTag, "currentTime: $currentTime")
                Log.d(logTag, "mils: $mils")
            } else {
                chronometer.base = smCurrentTime
            }
        }


//        sessionManager = SessionManager(this.activity?.applicationContext)
//        format = SimpleDateFormat("hh:mm:ss aa")
//        // Get current time
//        currentTime = format.format(Date())
//
//        // Get flag from session manager
//        var flag = sessionManager.flag
//        if (!flag){
//            // Set current time
//            sessionManager.currentTime = currentTime
//            sessionManager.flag = true
//            chronometer.start()
//        } else {
//            // Get session manager current time
//            val sessionManagerCurrentTime = sessionManager.currentTime
//            // Convert String to date
//            val date1 = format.parse(sessionManagerCurrentTime)
//            val date2 = format.parse(currentTime)
//            // Calculate time difference
//            val mils: Long = date2.time - date1.time
//            chronometer.base = SystemClock.elapsedRealtime() - mils
//            chronometer.start()
//        }
//
//        buttonReset.setOnClickListener {
//            chronometer.base = SystemClock.elapsedRealtime()
//            sessionManager.currentTime = currentTime
//            chronometer.start()
//        }

//        buttonStart.setOnClickListener {
//            // If the chronometer is not running, start it
//            if (!chronometer.isRunning) {
//                chronometer.start()
//                sessionManager.stopwatchRunning = true
//            }
//        }
//        buttonPause.setOnClickListener {
//            // If the chronometer is running, stop it
//            if (chronometer.isRunning) {
//                chronometer.stop()
//                sessionManager.stopwatchRunning = false
//            }
//        }
//        buttonReset.setOnClickListener {
//            chronometer.reset()
//            sessionManager.stopwatchRunning = false
//        }

        buttonStart.setOnClickListener {
            // If the chronometer is not running, start it
            if (!sessionManager.stopwatchRunning) {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                sessionManager.stopwatchRunning = true
                sessionManager.stopwatchRunningBackground = true
            }
        }
        buttonPause.setOnClickListener {
            // If the chronometer is running, stop it
            if (sessionManager.stopwatchRunning) {
                chronometer.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                sessionManager.stopwatchRunning = false
            }
        }
        buttonReset.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            pauseOffset = 0
            chronometer.stop()
            sessionManager.stopwatchRunning = false
            sessionManager.stopwatchRunningBackground = false
        }

//        if (savedInstanceState != null) {
//            chronometer.restoreInstanceState(savedInstanceState)
//        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        chronometer.saveInstanceState(outState)
//    }

    private fun startStopwatch() {

    }

    override fun onPause() {
        super.onPause()

        if (sessionManager.stopwatchRunningBackground) {
            Log.d(logTag, "Chronometer base when onPause called: ${chronometer.base}")
            sessionManager.currentTime = chronometer.base
        }
    }
}