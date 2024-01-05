package com.istudio.app.di


import android.app.NotificationManager
import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import com.istudio.app.R
import com.istudio.app.service.ServiceHelper
import com.istudio.app.util.Constants.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @OptIn(ExperimentalAnimationApi::class)
    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {

        val resource = context.resources
        // -------------> Strings
        val title = resource.getString(R.string.app_name)
        val strStop = resource.getString(R.string.str_stop)
        val strCancel = resource.getString(R.string.str_cancel)
        val strCounter = resource.getString(R.string.counter_initial_text)
        // -------------> Intents
        val stopIntent = ServiceHelper.stopPendingIntent(context)
        val cancelIntent = ServiceHelper.cancelPendingIntent(context)
        val clickActionIntent = ServiceHelper.clickPendingIntent(context)

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(strCounter)
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setOngoing(true)
            .addAction(0, strStop, stopIntent)
            .addAction(0, strCancel,cancelIntent)
            .setContentIntent(clickActionIntent)
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}