package com.fomichev.alarmmessager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import com.fomichev.alarmmessager.domain.AlarmCFG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmStarter @Inject constructor(@ApplicationContext val appContext: Context) {
    val PENDING_INTENT_FLAG_IMMUTABLE =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else 0
    companion object{
        val ALARM = "Alarm"
        val MSG = "Msg"
    }

    val alarmIntent = Intent(appContext, AlarmReceiver::class.java).putExtra("aim", ALARM)
    val alarmPendingIntent: PendingIntent =
        PendingIntent.getBroadcast(appContext, 0, alarmIntent, PENDING_INTENT_FLAG_IMMUTABLE)

    val msgIntent = Intent(appContext, AlarmReceiver::class.java).putExtra("aim", MSG)
    val msgPendingIntent: PendingIntent =
        PendingIntent.getBroadcast(appContext, 1, msgIntent, PENDING_INTENT_FLAG_IMMUTABLE)

    val am = appContext.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager

    fun startAlarm(alarmCfg: AlarmCFG) {
        am.set(
            AlarmManager.RTC_WAKEUP,
            alarmCfg.startTime + alarmCfg.timeToAlarm * 1000 * 60,
            alarmPendingIntent
        )
        am.set(
            AlarmManager.RTC_WAKEUP,
            alarmCfg.startTime + (alarmCfg.timeToAlarm + alarmCfg.timeToMsg) * 1000 * 60,
            msgPendingIntent
        )
    }

    fun stopAlarm() {
        am.cancel(alarmPendingIntent)
        am.cancel(msgPendingIntent)
    }
}