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
    val intent = Intent(appContext, AlarmReceiver::class.java)
    val pIntent: PendingIntent =
        PendingIntent.getBroadcast(appContext, 0, intent, PENDING_INTENT_FLAG_IMMUTABLE)
    val am = appContext.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager

    fun startAlarm(alarmCfg: AlarmCFG) {
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 4000, pIntent)
    }

    fun stopAlarm() {
        am.cancel(pIntent)
    }
}