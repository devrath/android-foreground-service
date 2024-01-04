package com.istudio.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.istudio.app.R
import com.istudio.app.util.Constants.NOTIFICATION_CHANNEL_ID
import com.istudio.app.util.Constants.NOTIFICATION_ID

class StopwatchService : Service() {

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

}