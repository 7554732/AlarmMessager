package com.fomichev.alarmmessager

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.core.content.ContextCompat.startForegroundService
import com.fomichev.alarmmessager.AlarmMessagerApplication.Companion.DIVISOR_ML_SEC
import com.fomichev.alarmmessager.AlarmMessagerApplication.Companion.DIVISOR_SEC_IN_MIN
import com.fomichev.alarmmessager.TimerService.Companion.CLASS_NAME
import com.fomichev.alarmmessager.TimerService.Companion.TIME_TO_END
import com.fomichev.alarmmessager.domain.AlarmCFG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class AlarmTimer: TimerService()
class MsgTimer: TimerService()
@Singleton
class TimerStarter  @Inject constructor(@ApplicationContext val appContext: Context) {
    val alarmIntent = Intent(appContext, AlarmTimer::class.java)
    val msgIntent = Intent(appContext, MsgTimer::class.java)

    fun startAlarm(alarmCfg: AlarmCFG) {
        startTimer(
            "com.fomichev.alarmmessager.AlarmReceiver",
            (alarmCfg.timeToAlarm * DIVISOR_SEC_IN_MIN * DIVISOR_ML_SEC).toLong(),
            alarmIntent
        )
        startTimer(
            "com.fomichev.alarmmessager.MsgReceiver",
            ((alarmCfg.timeToAlarm + alarmCfg.timeToMsg ) * DIVISOR_SEC_IN_MIN * DIVISOR_ML_SEC).toLong(),
            msgIntent
        )
    }

    private fun startTimer(cls: String, time_to_end: Long, intent: Intent){
        intent.putExtra(CLASS_NAME, cls)
        intent.putExtra(TIME_TO_END, time_to_end)
        startForegroundService(appContext, intent)
    }

    fun stopAlarm() {
        appContext.stopService(alarmIntent)
        appContext.stopService(msgIntent)
    }

    fun bindTimer(connection: ServiceConnection, className: String) {
        val cls = Class.forName(className)
        val intent = Intent(appContext, cls)
        appContext.bindService(intent, connection, 0)
    }

    fun unbindTimer(connection: ServiceConnection) {
        try {
            appContext.unbindService(connection)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}