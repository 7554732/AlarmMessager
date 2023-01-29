package com.fomichev.alarmmessager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import com.fomichev.alarmmessager.TimerService.Companion.CLASS_NAME
import com.fomichev.alarmmessager.TimerService.Companion.TIME_TO_END
import com.fomichev.alarmmessager.domain.AlarmCFG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerStarter  @Inject constructor(@ApplicationContext val appContext: Context) {
    val alarmIntent = Intent(appContext, TimerService::class.java)
    val msgIntent = Intent(appContext, TimerService::class.java)

    fun startAlarm(alarmCfg: AlarmCFG) {
        startTimer(
            "com.fomichev.alarmmessager.AlarmReceiver",
            (alarmCfg.timeToAlarm * 1000).toLong(),
            alarmIntent
        )
        startTimer(
            "com.fomichev.alarmmessager.MsgReceiver",
            (alarmCfg.timeToMsg * 1000).toLong(),
            msgIntent
        )
    }

    private fun startTimer(cls: String, time_to_end: Long, intent: Intent){
        alarmIntent.putExtra(TIME_TO_END, cls)
        alarmIntent.putExtra(CLASS_NAME, time_to_end)
        startForegroundService(appContext, intent)
    }

    fun stopAlarm() {
        appContext.stopService(alarmIntent)
        appContext.stopService(msgIntent)
    }
}