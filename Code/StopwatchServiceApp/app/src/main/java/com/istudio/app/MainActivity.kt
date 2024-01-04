package com.istudio.app

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.istudio.app.service.StopwatchService
import com.istudio.app.ui.theme.StopWatchServiceAppTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= (Build.VERSION_CODES.TIRAMISU)) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
            )
        }

        setContent {
            StopWatchServiceAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrentScreen()
                }
            }
        }
    }
}


@Composable
fun CurrentScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            Intent(context,StopwatchService::class.java).also {
                it.action = StopwatchService.Actions.START.toString()
                context.startService(it)
            }
        }) {
            Text(text = "Start Service")
        }
        Button(onClick = {
            Intent(context,StopwatchService::class.java).also {
                it.action = StopwatchService.Actions.STOP.toString()
                context.startService(it)
            }
        }) {
            Text(text = "Stop Service")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StopWatchServiceAppTheme {
        CurrentScreen()
    }
}