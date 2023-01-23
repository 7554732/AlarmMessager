package com.fomichev.alarmmessager.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomichev.alarmmessager.AlarmStarter
import com.fomichev.alarmmessager.domain.AlarmCFG
import com.fomichev.alarmmessager.domain.Msg
import com.fomichev.alarmmessager.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AlarmMessagerViewModel @Inject constructor(
        val alarmStarter: AlarmStarter,
        val settingsRepository: SettingsRepository
    ) : ViewModel() {

    val alarmCfgInit get() = runBlocking { settingsRepository.alarmCfgFlow.first() }
    val msg get() = runBlocking { settingsRepository.msgFlow.first() }

    val alarmFlow = settingsRepository.alarmCfgFlow

    fun onStart(_alarmCfg: AlarmCFG) {
        viewModelScope.launch {
            settingsRepository.saveAlarmCFG(_alarmCfg)
        }
        if(_alarmCfg.isStarted)
            alarmStarter.startAlarm(_alarmCfg)
        else
            alarmStarter.stopAlarm()
        Log.d("AlarmMessagerViewModel ", " " + _alarmCfg)
    }

    fun onMsgSave(_msg: Msg) {
        viewModelScope.launch {
            settingsRepository.saveMsg(_msg)
        }
        Log.d("AlarmMessagerViewModel ", " " + _msg)
    }
}
