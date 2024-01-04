package com.istudio.app

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.istudio.app.service.StopwatchService
import com.istudio.app.ui.theme.StopWatchServiceAppTheme
import dagger.hilt.android.AndroidEntryPoint


/**
 * 1) Why we have used bound service ?
 * --> We have used bound service because, When application is destroyed & recreated when we start timer and kill activity. Once we reopen app, it has to be updated so bound service helps us achieve it.
 *
 */

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // We Keep "isBound" Flag keep track when the service is connected/disconnected
    private var isBound by mutableStateOf(false)
    // Reference to the service used to display the stop clock
    private lateinit var stopwatchService: StopwatchService

    /** ********************** Life-Cycle-Methods ********************** **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnCreate()
    }

    override fun onStart() {
        super.onStart()
        initOnStart()
    }

    override fun onStop() {
        super.onStop()
        initOnStop()
    }
    /** ********************** Life-Cycle-Methods ********************** **/


    /** ********************** Init-Methods **************************** **/
    private fun initOnCreate() {
        setContent {
            StopWatchServiceAppTheme {
                if (isBound) {
                    // Only if the service is connected, Then show the UI
                    MainScreen(stopwatchService = stopwatchService)
                }
            }
        }
        requestPermissions(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun initOnStart() {
        // Bind the service
        Intent(this, StopwatchService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    private fun initOnStop() {
        // Unbind the service
        unbindService(connection)
        // Update the flag that service is unbinded
        isBound = false
    }
    /** ********************** Init-Methods **************************** **/


    /** ********************** Other Functions ************************* **/
    /**
     * Initialize the service
     * StartService:-> To be done in OnStart
     * StartService:-> TO be done in OnDestroy
     */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We get the binder instance
            val binder = service as StopwatchService.StopwatchBinder
            // Using the binder, We get the instance of the service to handle and control it
            stopwatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }


    /**
     * Request permission to display the notification
     */
    private fun requestPermissions(vararg permissions: String) {

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach {
                Log.d("MainActivity", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }
    /** ********************** Other Functions ************************* **/

}