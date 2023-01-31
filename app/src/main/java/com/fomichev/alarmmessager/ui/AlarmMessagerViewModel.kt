package com.fomichev.alarmmessager.ui

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomichev.alarmmessager.AlarmWakeLock
import com.fomichev.alarmmessager.TimerService
import com.fomichev.alarmmessager.TimerStarter
import com.fomichev.alarmmessager.domain.AlarmCFG
import com.fomichev.alarmmessager.domain.Msg
import com.fomichev.alarmmessager.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AlarmMessagerViewModel @Inject constructor(
    val timerStarter: TimerStarter,
    val wakeLock: AlarmWakeLock,
    val settingsRepository: SettingsRepository
) : ViewModel() {

    val alarmCfgInit get() = runBlocking { settingsRepository.alarmCfgFlow.first() }
    val msg get() = runBlocking { settingsRepository.msgFlow.first() }

    val alarmFlow = settingsRepository.alarmCfgFlow

    fun onStart(_alarmCfg: AlarmCFG) {
        viewModelScope.launch {
            settingsRepository.saveAlarmCFG(_alarmCfg)
        }
        if(_alarmCfg.isStarted) {
            timerStarter.startAlarm(_alarmCfg)
            wakeLock.setWakeLock(true)
        }
        else {
            timerStarter.stopAlarm()
            wakeLock.setWakeLock(false)
        }
        Log.d("AlarmMessagerViewModel ", " " + _alarmCfg)
    }

    fun onMsgSave(_msg: Msg) {
        viewModelScope.launch {
            settingsRepository.saveMsg(_msg)
        }
        Log.d("AlarmMessagerViewModel ", " " + _msg)
    }

    private var msgTimerBinder: TimerService.TimerServiceBinder? = null

    private var _timeLeftToMsg = MutableLiveData(0L)
    val timeLeftToMsg: MediatorLiveData<Long> = MediatorLiveData<Long>()
    init {
        timeLeftToMsg.addSource(_timeLeftToMsg, { t -> timeLeftToMsg.value = t })
    }

    private val msgTimerConnection: ServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            msgTimerBinder = binder as TimerService.TimerServiceBinder
            timeLeftToMsg.addSource(msgTimerBinder!!.timeToEnd, { t -> timeLeftToMsg.value = t })
            Timber.d( " msgTimerConnection " + (msgTimerBinder != null) + " "+ timeLeftToMsg?.value)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timeLeftToMsg.removeSource(msgTimerBinder!!.timeToEnd)
            msgTimerBinder = null
            _timeLeftToMsg.value = 0L
            Timber.d(" msgTimerConnection " + (msgTimerBinder != null)+ " " + timeLeftToMsg?.value)
            bindMsgTimer()
        }
    }

    init{
        bindMsgTimer()
    }

    private fun bindMsgTimer() {
        if(msgTimerBinder != null) return
        timerStarter.bindTimer(msgTimerConnection, "com.fomichev.alarmmessager.MsgTimer")
    }

    private fun unbindMsgTimer() {
        timerStarter.unbindTimer(msgTimerConnection)
    }

    fun freeResources() {
        unbindMsgTimer()
        msgTimerBinder = null
    }
}
