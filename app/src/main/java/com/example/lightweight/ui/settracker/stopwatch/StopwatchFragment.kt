package com.example.lightweight.ui.settracker.stopwatch

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lightweight.R
import com.google.android.material.button.MaterialButton

class StopwatchFragment : Fragment(R.layout.fragment_stopwatch) {

    private val logTag = "StopwatchFragment"

    private var isStopwatchRunning = false

    private lateinit var statusReceiver: BroadcastReceiver
    private lateinit var timeReceiver: BroadcastReceiver

    private lateinit var stopwatchValueTextView: TextView
    private lateinit var resetImageView: ImageView
    private lateinit var toggleButton: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stopwatchValueTextView = view.findViewById(R.id.stopwatch_value_text_view)
        resetImageView = view.findViewById(R.id.reset_image_view)
        toggleButton = view.findViewById(R.id.toggle_button)

        toggleButton.setOnClickListener {
            if (isStopwatchRunning) pauseStopwatch() else startStopwatch()
        }

        resetImageView.setOnClickListener {
            resetStopwatch()
        }
    }

    override fun onResume() {
        super.onResume()

        getStopwatchStatus()

        // Receiving stopwatch status from service
        val statusFilter = IntentFilter()
        statusFilter.addAction(StopwatchService.STOPWATCH_STATUS)
        statusReceiver = object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(p0: Context?, p1: Intent?) {
                val isRunning = p1?.getBooleanExtra(StopwatchService.IS_STOPWATCH_RUNNING, false)!!
                isStopwatchRunning = isRunning
                val timeElapsed = p1.getIntExtra(StopwatchService.TIME_ELAPSED, 0)

                updateLayout(isStopwatchRunning)
                updateStopwatchValue(timeElapsed)
            }
        }
        requireActivity().registerReceiver(statusReceiver, statusFilter)

        // Receiving time values from service
        val timeFilter = IntentFilter()
        timeFilter.addAction(StopwatchService.STOPWATCH_TICK)
        timeReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val timeElapsed = p1?.getIntExtra(StopwatchService.TIME_ELAPSED, 0)!!
                updateStopwatchValue(timeElapsed)
            }
        }
        requireActivity().registerReceiver(timeReceiver, timeFilter)
    }

    @SuppressLint("SetTextI18n")
    private fun updateStopwatchValue(timeElapsed: Int) {
        val hours: Int = (timeElapsed / 60) / 60
        val minutes: Int = timeElapsed / 60
        val seconds: Int = timeElapsed % 60
        stopwatchValueTextView.text =
            "${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}"
    }

    private fun updateLayout(isStopwatchRunning: Boolean) {
        if (isStopwatchRunning) {
            toggleButton.icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_pause_24)
            resetImageView.visibility = View.INVISIBLE
        } else {
            toggleButton.icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_play_arrow_24)
            resetImageView.visibility = View.VISIBLE
        }
    }

    private fun getStopwatchStatus() {
        val stopwatchService = Intent(requireContext(), StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.GET_STATUS)
        requireActivity().startService(stopwatchService)
    }

    private fun startStopwatch() {
        val stopwatchService = Intent(requireContext(), StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.START)
        requireActivity().startService(stopwatchService)
    }

    private fun pauseStopwatch() {
        val stopwatchService = Intent(requireContext(), StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.PAUSE)
        requireActivity().startService(stopwatchService)
    }

    private fun resetStopwatch() {
        val stopwatchService = Intent(requireContext(), StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.RESET)
        requireActivity().startService(stopwatchService)
    }

    private fun moveToForeground() {
        val stopwatchService = Intent(requireContext(), StopwatchService::class.java)
        stopwatchService.putExtra(
            StopwatchService.STOPWATCH_ACTION,
            StopwatchService.MOVE_TO_FOREGROUND
        )
        requireActivity().startService(stopwatchService)
    }

    private fun moveToBackground() {
        val stopwatchService = Intent(requireContext(), StopwatchService::class.java)
        stopwatchService.putExtra(
            StopwatchService.STOPWATCH_ACTION,
            StopwatchService.MOVE_TO_BACKGROUND
        )
        requireActivity().startService(stopwatchService)
    }

    override fun onStart() {
        super.onStart()

        // Moving the service to background when the app is visible
        moveToBackground()
    }

    override fun onPause() {
        super.onPause()

        // Unregister the receivers to avoid leaks
        requireActivity().unregisterReceiver(statusReceiver)
        requireActivity().unregisterReceiver(timeReceiver)

        // Moving the service to foreground when the app is in background / not visible
        moveToForeground()
    }
}