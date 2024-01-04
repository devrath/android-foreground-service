package com.istudio.app

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.istudio.app.service.ServiceHelper
import com.istudio.app.service.ServiceHelper.leftButtonAction
import com.istudio.app.service.ServiceHelper.leftButtonText
import com.istudio.app.service.ServiceHelper.rightButtonAction
import com.istudio.app.service.StopwatchService
import com.istudio.app.ui.composables.AppButton
import com.istudio.app.ui.composables.AppText

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

    val isRightActionEnabled = seconds != "00" && currentState != ServiceHelper.StopwatchState.Started

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

            val buttonModifier = Modifier.weight(1f).fillMaxHeight(0.8f)

            AppButton(
                modifier = buttonModifier,
                text = leftButtonText(currentState),
                onClick = { leftButtonAction(context, currentState) }
            )

            Spacer(modifier = Modifier.width(30.dp))

            AppButton(
                modifier = buttonModifier,
                enabled = isRightActionEnabled,
                text = "Cancel",
                onClick = {
                    rightButtonAction(context)
                },
            )
        }
    }
}
