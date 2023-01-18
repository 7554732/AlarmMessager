package com.fomichev.alarmmessager.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fomichev.alarmmessager.AlarmStarter
import com.fomichev.alarmmessager.domain.AlarmCFG
import com.fomichev.alarmmessager.domain.Msg
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmMessagerViewModel @Inject constructor(val alarmStarter: AlarmStarter) : ViewModel() {
    var alarmCfg = AlarmCFG()
    var msg = Msg()

    fun onStart(_alarmCfg: AlarmCFG) {
        alarmCfg = _alarmCfg
        Log.d("AlarmMessagerViewModel ", " " + alarmCfg)
    }

    fun onMsgSave(_msg: Msg) {
        msg = _msg
        Log.d("AlarmMessagerViewModel ", " " + msg)
    }
}
