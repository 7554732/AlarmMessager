package com.fomichev.alarmmessager.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fomichev.alarmmessager.AlarmStarter
import com.fomichev.alarmmessager.domain.AlarmCFG
import com.fomichev.alarmmessager.domain.Msg
import com.fomichev.alarmmessager.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmMessagerViewModel @Inject constructor(
        val alarmStarter: AlarmStarter,
        val settingsRepository: SettingsRepository
    ) : ViewModel() {

    var alarmCfg = AlarmCFG()
    var msg = Msg()

    val settings = settingsRepository.exampleCounterFlow

    init {
        viewModelScope.launch {
            settings.collectLatest {
                Log.d("exampleCounterFlow ", "" + it)
            }
        }
    }
    fun onStart(_alarmCfg: AlarmCFG) {
        viewModelScope.launch {
            settingsRepository.incrementCounter()
        }
        alarmCfg = _alarmCfg
        if(alarmCfg.isStarted)
            alarmStarter.startAlarm(alarmCfg)
        else
            alarmStarter.stopAlarm()
        Log.d("AlarmMessagerViewModel ", " " + alarmCfg)
    }

    fun onMsgSave(_msg: Msg) {
        msg = _msg
        Log.d("AlarmMessagerViewModel ", " " + msg)
    }
}
