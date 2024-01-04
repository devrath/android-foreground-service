package com.istudio.app.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.istudio.app.R
import com.istudio.app.util.Constants.NOTIFICATION_CHANNEL_ID
import com.istudio.app.util.Constants.NOTIFICATION_ID
import com.istudio.app.util.StopwatchState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StopwatchService : Service() {

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(StopwatchState.Idle)
        private set



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * This method is triggered when another android component sends intent to the running service
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action){
            Actions.START.toString() ->{
                start()
            }
            Actions.STOP.toString() ->{
                stopSelf()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat
            .Builder(this,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Stop Watch")
            .setContentText("Content of the notification")
            .build()

        startForeground(NOTIFICATION_ID,notification)
    }


    enum class Actions{
        START, STOP
    }


    inner class StopwatchBinder : Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }

}