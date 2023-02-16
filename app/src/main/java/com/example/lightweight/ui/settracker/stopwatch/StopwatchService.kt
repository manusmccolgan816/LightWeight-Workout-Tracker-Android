package com.example.lightweight.ui.settracker.stopwatch

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.example.lightweight.R
import com.example.lightweight.ui.MainActivity
import java.util.*

class StopwatchService : Service() {

    private val logTag = "StopwatchService"

    companion object {
        //Channel ID for notifications
        const val CHANNEL_ID = "Stopwatch_Notifications"

        // Service actions
        const val START = "START"
        const val PAUSE = "PAUSE"
        const val RESET = "RESET"
        const val GET_STATUS = "GET_STATUS"
        const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
        const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"

        // Intent extras
        const val STOPWATCH_ACTION = "STOPWATCH_ACTION"
        const val TIME_ELAPSED = "TIME_ELAPSED"
        const val IS_STOPWATCH_RUNNING = "IS_STOPWATCH_RUNNING"

        // Intent actions
        const val STOPWATCH_TICK = "STOPWATCH_TICK"
        const val STOPWATCH_STATUS = "STOPWATCH_STATUS"
    }

    private var timeElapsed: Int = 0
    private var isStopWatchRunning = false

    private var updateTimer = Timer()
    private var stopwatchTimer = Timer()

    // Getting access to the NotificationManager
    private lateinit var notificationManager: NotificationManager

    /**
     * The system calls onBind() method to retrieve the IBinder only when the first client binds.
     * The system then delivers the same IBinder to any additional clients that bind, without
     * calling onBind() again.
     */
    override fun onBind(intent: Intent?): IBinder? {
        Log.d(logTag, "entered onBind")
        return null
    }

    /**
     * onStartCommand() is called every time a client starts the service using
     * startService(intent: Intent)
     * We will check for what action this service has been called for and then perform the action
     * accordingly. The action is extracted from the intent that is used to start the service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel()
        getNotificationManager()

        val action = intent?.getStringExtra(STOPWATCH_ACTION)!!

        Log.d(logTag, "onStartCommand Action: $action")

        when (action) {
            START -> startStopwatch()
            PAUSE -> pauseStopwatch()
            RESET -> resetStopwatch()
            GET_STATUS -> sendStatus()
            MOVE_TO_FOREGROUND -> moveToForeground()
            MOVE_TO_BACKGROUND -> moveToBackground()
        }

        return START_STICKY
    }

    private fun createChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Stopwatch",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.setSound(null, null)
        notificationChannel.setShowBadge(true)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }

    /**
     * This function is responsible for broadcasting the status of the stopwatch. It broadcasts
     * whether the stopwatch is running and the time elapsed.
     */
    private fun sendStatus() {
        val statusIntent = Intent()
        statusIntent.action = STOPWATCH_STATUS
        statusIntent.putExtra(IS_STOPWATCH_RUNNING, isStopWatchRunning)
        statusIntent.putExtra(TIME_ELAPSED, timeElapsed)
        sendBroadcast(statusIntent)
    }

    /**
     * This function starts the stopwatch. A timer is started and timeElapsed is increased by 1
     * every second and the value is broadcast with the action of STOPWATCH_TICK.
     * An activity or fragment can receive this broadcast to get access to the time elapsed.
     */
    private fun startStopwatch() {
        isStopWatchRunning = true

        sendStatus()

        stopwatchTimer = Timer()
        stopwatchTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val stopwatchIntent = Intent()
                stopwatchIntent.action = STOPWATCH_TICK

                timeElapsed++

                stopwatchIntent.putExtra(TIME_ELAPSED, timeElapsed)
                sendBroadcast(stopwatchIntent)
            }
        }, 0, 1000)
    }

    /**
     * Pauses the stopwatch and sends an update of its current state.
     */
    private fun pauseStopwatch() {
        stopwatchTimer.cancel()
        isStopWatchRunning = false
        sendStatus()
    }

    /**
     * Resets the stopwatch and sends an update of its current state.
     */
    private fun resetStopwatch() {
        pauseStopwatch()
        timeElapsed = 0
        sendStatus()
    }

    /**
     * This function is responsible for building and returning a Notification with the current
     * state of the stopwatch along with the timeElapsed.
     */
    private fun buildNotification(): Notification {
        val title = if (isStopWatchRunning) {
            "Stopwatch is running"
        } else {
            "Stopwatch is paused"
        }

        val hours: Int = timeElapsed.div(60).div(60)
        val minutes: Int = timeElapsed.div(60)
        val seconds: Int = timeElapsed.rem(60)

//        val intent = Intent(this, MainActivity::class.java)
//        val pIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setOngoing(true)
            .setContentText(
                "${"%02d".format(hours)}:${"%02d".format(minutes)}:${
                    "%02d".format(
                        seconds
                    )
                }"
            )
            .setColorized(true)
            .setColor(Color.parseColor("#BEAEE2"))
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setOnlyAlertOnce(true)
//            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()
    }


    /**
     * Uses notificationManager to update the existing notification with the new notification.
     */
    private fun updateNotification() {
        notificationManager.notify(
            1,
            buildNotification()
        )
    }

    /**
     * This is triggered when the app is no longer visible to the user. It checks if the stopwatch
     * is running, and starts a foreground service with the notification if it is.
     * Another timer is run to update the notification every second.
     */
    private fun moveToForeground() {
        if (isStopWatchRunning) {
            startForeground(1, buildNotification())

            updateTimer = Timer()

            updateTimer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    updateNotification()

                }
            }, 0, 1000)
        }
    }

    /**
     * This is triggered when the app is visible to the user again. It cancels the timer which was
     * updating the notification every second, as well as stopping the foreground service and
     * removing the notification.
     */
    private fun moveToBackground() {
        updateTimer.cancel()
        stopForeground(true)
    }
}