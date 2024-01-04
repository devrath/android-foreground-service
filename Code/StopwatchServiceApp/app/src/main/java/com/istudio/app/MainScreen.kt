package com.istudio.app

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.istudio.app.service.ServiceHelper
import com.istudio.app.service.StopwatchService
import com.istudio.app.ui.composables.AppText
import com.istudio.app.util.Constants.ACTION_SERVICE_CANCEL
import com.istudio.app.util.Constants.ACTION_SERVICE_START
import com.istudio.app.util.Constants.ACTION_SERVICE_STOP
import com.istudio.app.util.StopwatchState

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@ExperimentalAnimationApi
@Composable
fun MainScreen(stopwatchService: StopwatchService) {

    // Context
    val context = LocalContext.current
    // Service state
    val currentState by stopwatchService.currentState

    // <-------------- Time states -------------->
    val hours by stopwatchService.hours
    val minutes by stopwatchService.minutes
    val seconds by stopwatchService.seconds
    // <-------------- Time states -------------->

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.weight(weight = 9f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(text = hours)
            AppText(text = " : ")
            AppText(text = minutes)
            AppText(text = " : ")
            AppText(text = seconds)
        }
        Row(modifier = Modifier.weight(weight = 1f)) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.8f),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
                        else ACTION_SERVICE_START
                    )
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentState == StopwatchState.Started) Color.Red else Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (currentState == StopwatchState.Started) "Stop"
                    else if ((currentState == StopwatchState.Stopped)) "Resume"
                    else "Start"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.8f),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context, action = ACTION_SERVICE_CANCEL
                    )
                },
                enabled = seconds != "00" && currentState != StopwatchState.Started,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}