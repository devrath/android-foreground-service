<h1 align="center">𝚊𝚗𝚍𝚛𝚘𝚒𝚍-𝚏𝚘𝚛𝚎𝚐𝚛𝚘𝚞𝚗𝚍-𝚜𝚎𝚛𝚟𝚒𝚌𝚎</h1>

<div align="center">

![Android-App-Development-With-Kotlin](https://github.com/devrath/android-foreground-service/assets/1456191/b0192969-46bb-4af5-958b-a0bb6506be87)

⚙️ 𝙵𝚘𝚛𝚎𝚐𝚛𝚘𝚞𝚗𝚍 𝚜𝚎𝚛𝚟𝚒𝚌𝚎 𝚝𝚘 𝚛𝚞𝚗 𝚊𝚗 𝚒𝚗𝚏𝚒𝚗𝚒𝚝𝚎 𝚝𝚊𝚜𝚔 𝚒𝚗 𝚊𝚗𝚍𝚛𝚘𝚒𝚍

</div>


<div align="center">

| `𝙲𝙾𝙽𝚃𝙴𝙽𝚃𝚂` |
| ---------- |
| [`What is a foreground service`](https://github.com/devrath/android-foreground-service/wiki/Creating-a-simple-foreground-service) |
| [`Creating a simple foreground service`](https://github.com/devrath/android-foreground-service/wiki/Creating-a-simple-foreground-service) |
| [`Demo`](https://github.com/devrath/android-foreground-service/blob/main/README.md#demo) |
| [`Starting service from background`](https://github.com/devrath/android-foreground-service?tab=readme-ov-file#starting-service-from-background) |

</div>

### `What is a foreground service`
* In Android, a foreground service is a type of service that has a higher priority than regular background services.
* It is designed to perform operations that are noticeable to the user, and its notification is shown to keep the user aware of ongoing tasks. When an app runs a foreground service, it is less likely to be killed by the system, even when resources are scarce.

### `Where a foreground service is used`
* Foreground services are often used for tasks that the user should be aware of and that have a visual or noticeable impact on the application. 
* For example, when an app is playing music in the background, downloading files, or providing navigation instructions, it might use a foreground service to ensure that the user is aware of these activities.

### `Why a foreground service is useful`
* The notification associated with a foreground service serves as a persistent visual indicator to the user, informing them that the app is running a foreground service.
* This helps users understand why certain operations are ongoing, even when the app is not in the foreground.
* It ensures that the user is informed about ongoing activities that might affect their interaction with the app.

### `Demo`

<div align="center">

  https://github.com/devrath/android-foreground-service/assets/1456191/ca78dc76-5b9e-4eb0-875a-3426f65d08c3

</div>


### `Starting service from background` 
* Apps that target Android 12 or higher can't start foreground services while the app is running in the background, except for a few special cases. 
* If an app tries to start a foreground service while the app runs in the background, and the foreground service doesn't satisfy one of the exceptional cases, the system throws a `ForegroundServiceStartNotAllowedException`.

### `Creating a simple foreground service`

<div align="center">

| `Contents` |
| ---------- |
| [`Define the constants needed`](https://github.com/devrath/AIDL-Connector/wiki/Creating-a-foreground-service#define-the-constants-needed) |
| [`Create a service class`](https://github.com/devrath/AIDL-Connector/wiki/Creating-a-foreground-service#create-a-service-class) |
| [`Define the channel creation`](https://github.com/devrath/AIDL-Connector/wiki/Creating-a-foreground-service#define-the-channel-creation) |
| [`Define the runtime permission`](https://github.com/devrath/AIDL-Connector/wiki/Creating-a-foreground-service#define-the-runtime-permission) |
| [`Define the permissions in the manifest`](https://github.com/devrath/AIDL-Connector/wiki/Creating-a-foreground-service#define-the-runtime-permission) |
| [`Declare the service tag in the manifest`](https://github.com/devrath/AIDL-Connector/wiki/Creating-a-foreground-service#declare-the-service-tag-in-the-manifest) |
| [`Initiate start and stop actions from your UI`](https://github.com/devrath/AIDL-Connector/wiki/Creating-a-foreground-service#initiate-start-and-stop-actions-from-your-ui) |

</div>




## `Define the constants needed`
```kotlin
object Constants {
    const val NOTIFICATION_CHANNEL_ID = "STOPWATCH_NOTIFICATION_ID"
    const val NOTIFICATION_CHANNEL_NAME = "STOPWATCH_NOTIFICATION"
    const val NOTIFICATION_ID = 10
}
```

## `Create a service class`
```kotlin
class StopwatchService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * This method is triggered when another Android component sends the intent to the running service
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
```

## `Define the channel creation`
**MyApplication.kt**
```kotlin
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= (Build.VERSION_CODES.O)){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME ,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
```

## `Define the runtime permission`
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= (Build.VERSION_CODES.TIRAMISU)) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
            )
        }
        setContent {
            // Content
        }
    }
}
```

## `Define the permissions in the manifest`
```xml
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## `Declare the service tag in the manifest`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!--- Define your permissions ---!>

    <application
        android:name=".MyApplication">
      
        <!-- Other codes -->

        <service
            android:name=".service.StopwatchService"
            android:foregroundServiceType="shortService"
            android:exported="false"/>

    </application>

</manifest>
```

## `Initiate start and stop actions from your UI`
```kotlin
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
```


## **`𝚂𝚞𝚙𝚙𝚘𝚛𝚝`** ☕
𝙸𝚏 𝚢𝚘𝚞 𝚏𝚎𝚎𝚕 𝚕𝚒𝚔𝚎 𝚜𝚞𝚙𝚙𝚘𝚛𝚝 𝚖𝚎 𝚊 𝚌𝚘𝚏𝚏𝚎𝚎 𝚏𝚘𝚛 𝚖𝚢 𝚎𝚏𝚏𝚘𝚛𝚝𝚜, 𝙸 𝚠𝚘𝚞𝚕𝚍 𝚐𝚛𝚎𝚊𝚝𝚕𝚢 𝚊𝚙𝚙𝚛𝚎𝚌𝚒𝚊𝚝𝚎 𝚒𝚝.</br>
<a href="https://www.buymeacoffee.com/devrath" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/yellow_img.png" alt="𝙱𝚞𝚢 𝙼𝚎 𝙰 𝙲𝚘𝚏𝚏𝚎𝚎" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>

## **`𝙲𝚘𝚗𝚝𝚛𝚒𝚋𝚞𝚝𝚎`** 🙋‍♂️
𝚁𝚎𝚊𝚍 [𝚌𝚘𝚗𝚝𝚛𝚒𝚋𝚞𝚝𝚒𝚘𝚗 𝚐𝚞𝚒𝚍𝚎𝚕𝚒𝚗𝚎𝚜](CONTRIBUTING.md) 𝚏𝚘𝚛 𝚖𝚘𝚛𝚎 𝚒𝚗𝚏𝚘𝚛𝚖𝚊𝚝𝚒𝚘𝚗 𝚛𝚎𝚐𝚊𝚛𝚍𝚒𝚗𝚐 𝚌𝚘𝚗𝚝𝚛𝚒𝚋𝚞𝚝𝚒𝚘𝚗.

## **`𝙵𝚎𝚎𝚍𝚋𝚊𝚌𝚔`** ✍️
𝙵𝚎𝚊𝚝𝚞𝚛𝚎 𝚛𝚎𝚚𝚞𝚎𝚜𝚝𝚜 𝚊𝚛𝚎 𝚊𝚕𝚠𝚊𝚢𝚜 𝚠𝚎𝚕𝚌𝚘𝚖𝚎, [𝙵𝚒𝚕𝚎 𝚊𝚗 𝚒𝚜𝚜𝚞𝚎 𝚑𝚎𝚛𝚎](https://github.com/devrath/android-foreground-service/issues/new).

## **`𝙵𝚒𝚗𝚍 𝚝𝚑𝚒𝚜 𝚙𝚛𝚘𝚓𝚎𝚌𝚝 𝚞𝚜𝚎𝚏𝚞𝚕`** ? ❤️
𝚂𝚞𝚙𝚙𝚘𝚛𝚝 𝚒𝚝 𝚋𝚢 𝚌𝚕𝚒𝚌𝚔𝚒𝚗𝚐 𝚝𝚑𝚎 ⭐ 𝚋𝚞𝚝𝚝𝚘𝚗 𝚘𝚗 𝚝𝚑𝚎 𝚞𝚙𝚙𝚎𝚛 𝚛𝚒𝚐𝚑𝚝 𝚘𝚏 𝚝𝚑𝚒𝚜 𝚙𝚊𝚐𝚎. ✌️

## **`𝙻𝚒𝚌𝚎𝚗𝚜𝚎`** ![Licence](https://img.shields.io/github/license/google/docsy) :credit_card:
𝚃𝚑𝚒𝚜 𝚙𝚛𝚘𝚓𝚎𝚌𝚝 𝚒𝚜 𝚕𝚒𝚌𝚎𝚗𝚜𝚎𝚍 𝚞𝚗𝚍𝚎𝚛 𝚝𝚑𝚎 𝙰𝚙𝚊𝚌𝚑𝚎 𝙻𝚒𝚌𝚎𝚗𝚜𝚎 𝟸.𝟶 - 𝚜𝚎𝚎 𝚝𝚑𝚎 [𝙻𝙸𝙲𝙴𝙽𝚂𝙴](https://github.com/devrath/android-foreground-service/blob/main/LICENSE) 𝚏𝚒𝚕𝚎 𝚏𝚘𝚛 𝚍𝚎𝚝𝚊𝚒𝚕𝚜.


<p align="center">
<a><img src="https://forthebadge.com/images/badges/built-for-android.svg"></a>
</p>
