package com.fomichev.alarmmessager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import com.fomichev.alarmmessager.AlarmStarter.Companion.ALARM
import com.fomichev.alarmmessager.AlarmStarter.Companion.MSG
import com.fomichev.alarmmessager.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
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
                    MSG -> {
                        val cfg = settingsRepository.alarmCfgFlow.first()
                        val msg = settingsRepository.msgFlow.first()
                        if(cfg.isStarted){
                            sendSMS(context, msg.phoneNumber, msg.text)
                            settingsRepository.setStarted(false)
                        }
                    }
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

    fun sendSMS(context: Context?, phoneNumber: String, msg: String) {
        if(context == null) return
        try {
            val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(
                    SmsManager::class.java
                )
            } else {
                SmsManager.getDefault()
            }
            val parts: ArrayList<String> = smsManager.divideMessage(msg)
            smsManager.sendMultipartTextMessage("+" + phoneNumber, null, parts, null, null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}