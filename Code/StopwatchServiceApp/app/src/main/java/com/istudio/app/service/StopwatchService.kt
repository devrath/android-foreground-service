package com.istudio.app.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.istudio.app.util.Constants.ACTION_SERVICE_CANCEL
import com.istudio.app.util.Constants.ACTION_SERVICE_START
import com.istudio.app.util.Constants.ACTION_SERVICE_STOP
import com.istudio.app.util.Constants.NOTIFICATION_CHANNEL_ID
import com.istudio.app.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.istudio.app.util.Constants.NOTIFICATION_ID
import com.istudio.app.util.Constants.STOPWATCH_STATE
import com.istudio.app.util.formatTime
import com.istudio.app.util.pad
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("RestrictedApi")
@AndroidEntryPoint
class StopwatchService : Service() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = StopwatchBinder()

    private var duration: Duration = Duration.ZERO
    private lateinit var timer: Timer

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(ServiceHelper.StopwatchState.Idle)
        private set


    override fun onBind(intent: Intent?) = binder


    /**
     * This method is triggered when another android component sends intent to the running service
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            it.getStringExtra(STOPWATCH_STATE)?.let { stringExtra ->
                // Notification click action handling
                when (stringExtra) {
                    ServiceHelper.StopwatchState.Started.name -> start()
                    ServiceHelper.StopwatchState.Stopped.name -> stop()
                    ServiceHelper.StopwatchState.Canceled.name -> cancel()
                }
            }

            it.action?.let { action ->
                // Button on-click flow action handling
                when (action) {
                    ACTION_SERVICE_START -> start()
                    ACTION_SERVICE_STOP -> stop()
                    ACTION_SERVICE_CANCEL -> cancel()
                }
            }
        }
    }

    /** ********************** MAIN SERVICE ACTIONS *************** **/
    private fun start() {
        // Set the UI
        setStopButton()
        // Update counter
        startStopwatch { hours, minutes, seconds ->
            updateNotification(hours = hours, minutes = minutes, seconds = seconds)
        }
        // Update service
        startForegroundService()
    }

    private fun stop() {
        // Set the UI
        setResumeButton()
        // Update service
        stopStopwatch()
    }

    private fun cancel() {
        // Set the UI
        cancelStopwatch()
        // Update counter
        stopStopwatch()
        // Update service
        stopForegroundService()
    }
    /** ********************** MAIN SERVICE ACTIONS *************** **/

    /** **************** STOP-WATCH ACTIONS ********************** **/
    private fun startStopwatch(onTick: (h: String, m: String, s: String) -> Unit) {
        currentState.value = ServiceHelper.StopwatchState.Started
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState.value = ServiceHelper.StopwatchState.Stopped
    }

    private fun cancelStopwatch() {
        duration = Duration.ZERO
        currentState.value = ServiceHelper.StopwatchState.Idle
        updateTimeUnits()
    }
    /** **************** STOP-WATCH ACTIONS ********************** **/


    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@StopwatchService.hours.value = hours.toInt().pad()
            this@StopwatchService.minutes.value = minutes.pad()
            this@StopwatchService.seconds.value = seconds.pad()
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        val formattedText = formatTime( hours, minutes, seconds)
        notificationManager.notify(
            NOTIFICATION_ID, notificationBuilder.setContentText(formattedText).build()
        )
    }

    private fun setStopButton() {
        val index = 0
        val title = "Stop"
        val intent = ServiceHelper.stopPendingIntent(baseContext)
        val notification = NotificationCompat.Action(0, title,intent)

        updateNotificationStructure(index, notification)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }


    private fun setResumeButton() {

        val index = 0
        val title = "Resume"
        val intent = ServiceHelper.resumePendingIntent(baseContext)
        val notification = NotificationCompat.Action(0, title,intent)

        updateNotificationStructure(index, notification)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun updateNotificationStructure(
        index: Int,
        notification: NotificationCompat.Action
    ) {
        notificationBuilder.mActions.apply {
            // Remove the existing notification
            removeAt(index)
            // Add new notification at same position
            add(index, notification)
        }
    }

    inner class StopwatchBinder : Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }

}