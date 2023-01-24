package com.fomichev.alarmmessager

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import com.fomichev.alarmmessager.AlarmStarter.Companion.ALARM
import com.fomichev.alarmmessager.AlarmStarter.Companion.MSG
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
                val aim = intent?.getStringExtra("aim")
                when (aim){
                    ALARM -> playSound(context, "alarm")
                    MSG -> settingsRepository.setStarted(false)
                }
                Log.d("AlarmReceiver", "onReceive" + aim)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun playSound(context: Context?, sound: String) {
        if(context == null) return
        val soundResource: Int = context.getResources().getIdentifier(
            sound,
            "raw",
            context.getPackageName()
        )
        val mp: MediaPlayer = MediaPlayer.create(context, soundResource)
        mp.setOnCompletionListener { mp ->
            mp!!.reset()
            mp!!.release()
        }
        mp.start()
    }
}