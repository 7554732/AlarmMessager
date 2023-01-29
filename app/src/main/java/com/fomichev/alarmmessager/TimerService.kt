package com.fomichev.alarmmessager

import android.app.NotificationChannel
import android.app.NotificationManager
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


class TimerService: Service() {
    companion object {
        val TIME_TO_END = "time_to_end"
        val CLASS_NAME = "class_name"
        val UPDATE_TIME: Long = 1000L
    }

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

        createNotificationChannel()
        startForeground(
            1, NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(endTime.toString())
                .setContentText("")
                .setShowWhen(false)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
                .setGroup(getString(R.string.app_name))
                .setGroupSummary(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL).build()
        )

        Timber.d("onStartCommand TimerService " + timeToEnd.value)
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
        Timber.d("onBind TimerService " + timeToEnd.value)
        return TimerServiceBinder()
    }

    inner class TimerServiceBinder : Binder() {
        val timeToEnd: LiveData<Long>
            get() = this@TimerService.timeToEnd
    }

    override fun onDestroy() {
        super.onDestroy()
        stopCircleTimerUpdater()
        Timber.d("TimerService onDestroy")
    }

    suspend fun runTimerUpdater() {
        while (timeToEnd.value!! > 0){
            delay(UPDATE_TIME)

            withContext(Dispatchers.Main) {
                _timeToEnd.value = endTime - System.currentTimeMillis()
            }
            Timber.d("onUpdate " + timeToEnd.value)
        }
        sendIntent()
        stopSelf()
    }

    private fun sendIntent() {
        val alarmIntent = Intent(this, cls).putExtra("aim",
            AlarmStarter.ALARM
        )
        sendBroadcast(alarmIntent)
    }

    private fun startCircleTimerUpdater() {
        circleTimerUpdater = scope.launch{
            runTimerUpdater()
        }
        Timber.d( "CircleTimerUpdater started")
    }


    fun stopCircleTimerUpdater() {
        circleTimerUpdater?.cancel()
        circleTimerUpdater = null
        Timber.d( "CircleTimerUpdater canceled")
    }

}