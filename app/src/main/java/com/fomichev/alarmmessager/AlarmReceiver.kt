package com.fomichev.alarmmessager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fomichev.alarmmessager.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    private val scope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult: PendingResult = goAsync()

        scope.launch(Dispatchers.Default) {
            try {
                settingsRepository.setStarted(false)
                Log.d("AlarmReceiver", "onReceive" + intent?.getStringExtra("aim"))
            } finally {
                pendingResult.finish()
            }
        }
    }
}