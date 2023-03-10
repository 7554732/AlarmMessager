package com.fomichev.alarmmessager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import timber.log.Timber


open class TimerService: Service() {
    val PENDING_INTENT_FLAG_IMMUTABLE =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
            PendingIntent.FLAG_IMMUTABLE
        else 0
    companion object {
        val TIME_TO_END = "time_to_end"
        val CLASS_NAME = "class_name"
        val UPDATE_TIME: Long = 1000L
    }
    val TAG = javaClass.simpleName

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var circleTimerUpdater: Job? = null

    private var endTime: Long = 0L

    private val _timeToEnd = MutableLiveData(0L)
    val timeToEnd: LiveData<Long>
        get() = _timeToEnd

    private lateinit var cls: Class<*>

    private val NOTIFICATION_CHANNEL_ID = "Timer_Service_Channel"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY

        _timeToEnd.value = intent.getLongExtra(TIME_TO_END, 0L)

        cls = Class.forName(
            intent.getStringExtra(CLASS_NAME)
        )

        //  restart countdown
        stopCircleTimerUpdater()
        if (timeToEnd.value!! > 0) {
            endTime = System.currentTimeMillis() + timeToEnd.value!!
            startCircleTimerUpdater()
        }

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent,
                    PENDING_INTENT_FLAG_IMMUTABLE)
            }

        createNotificationChannel()
        startForeground(
            1, NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(timeToString(endTime, "y-M-d H:m:s"))
                .setContentText("")
                .setShowWhen(false)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
                .setGroup(getString(R.string.app_name))
                .setGroupSummary(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build()
        )

        Timber.d(TAG + " onStartCommand " + timeToEnd.value)
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, getString(R.string.app_name), importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d(TAG + " onBind TimerService " + timeToEnd.value)
        return TimerServiceBinder()
    }

    inner class TimerServiceBinder : Binder() {
        val timeToEnd: LiveData<Long>
            get() = this@TimerService.timeToEnd
    }

    override fun onDestroy() {
        super.onDestroy()
        stopCircleTimerUpdater()
        Timber.d(TAG + " onDestroy")
    }

    suspend fun runTimerUpdater() {
        while (timeToEnd.value!! > 0){
            delay(UPDATE_TIME)

            withContext(Dispatchers.Main) {
                _timeToEnd.value = endTime - System.currentTimeMillis()
            }
            Timber.d(TAG + " onUpdate " + timeToEnd.value)
        }
        sendIntent()
        stopSelf()
    }

    private fun sendIntent() {
        val intent = Intent(this, cls)
        sendBroadcast(intent)
    }

    private fun startCircleTimerUpdater() {
        circleTimerUpdater = scope.launch{
            runTimerUpdater()
        }
        Timber.d( TAG + " CircleTimerUpdater started")
    }


    fun stopCircleTimerUpdater() {
        circleTimerUpdater?.cancel()
        circleTimerUpdater = null
        Timber.d( TAG + " CircleTimerUpdater canceled")
    }

}