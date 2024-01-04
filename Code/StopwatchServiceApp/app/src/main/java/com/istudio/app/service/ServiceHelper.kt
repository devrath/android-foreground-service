package com.istudio.app.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import com.istudio.app.MainActivity
import com.istudio.app.util.Constants
import com.istudio.app.util.Constants.CANCEL_REQUEST_CODE
import com.istudio.app.util.Constants.CLICK_REQUEST_CODE
import com.istudio.app.util.Constants.RESUME_REQUEST_CODE
import com.istudio.app.util.Constants.STOPWATCH_STATE
import com.istudio.app.util.Constants.STOP_REQUEST_CODE

@ExperimentalAnimationApi
object ServiceHelper {

    private val flag =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE
        else
            0

    /** ************************ Pending Intents  ************************ **/
    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, StopwatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, StopwatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Started.name)
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, StopwatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Canceled.name)
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }
    /** ************************ Pending Intents  ************************ **/

    fun toggleLeftButtonAction(currentState: StopwatchState): String {
        if (currentState == StopwatchState.Started){
            // If the service is already in started state --> Stop it
            return Constants.ACTION_SERVICE_STOP
        }else{
            // If the service is in stopped/resume state --> Start it
            return Constants.ACTION_SERVICE_START
        }
    }

    fun leftButtonText(currentState: StopwatchState): String {
        when (currentState) {
            StopwatchState.Started -> {
                // If the current service state is started ---> Display "Stop" text
                return "Stop"
            }
            StopwatchState.Stopped -> {
                // If the current service state is stopped ---> Display "Resume" text
                return "Resume"
            }
            else -> {
                // Else display "Start" text
                return "Start"
            }
        }
    }


    /** ************************ Service triggers  ************************ **/

    /** ************ Actions  ************ **/
    fun leftButtonAction(
        context: Context,
        currentState: StopwatchState
    ) {
        triggerForegroundService(
            context = context,
            action = toggleLeftButtonAction(currentState)
        )
    }

    fun rightButtonAction(context: Context) {
        triggerForegroundService(
            context = context,
            action = Constants.ACTION_SERVICE_CANCEL
        )
    }
    /** ************ Actions  ************ **/

    private fun triggerForegroundService(context: Context, action: String) {
        Intent(context, StopwatchService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }

    /** ************************ Service triggers  ************************ **/


    enum class StopwatchState {
        Idle, Started, Stopped, Canceled
    }

}