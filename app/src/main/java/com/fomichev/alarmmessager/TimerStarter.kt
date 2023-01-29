package com.fomichev.alarmmessager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import com.fomichev.alarmmessager.TimerService.Companion.TIME_TO_END
import com.fomichev.alarmmessager.domain.AlarmCFG
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerStarter  @Inject constructor(@ApplicationContext val appContext: Context) {
    val intent = Intent(appContext, TimerService::class.java)

    fun startAlarm(alarmCfg: AlarmCFG) {
        intent.putExtra(TIME_TO_END, (alarmCfg.timeToAlarm * 1000).toLong() )
        startForegroundService(appContext, intent)
    }

    fun stopAlarm() {
        appContext.stopService(intent)
    }
}