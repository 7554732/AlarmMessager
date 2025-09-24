package com.fomichev.alarmmessager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import com.fomichev.alarmmessager.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MsgReceiver:  BroadcastReceiver() {

    private val scope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var settingsRepository: SettingsRepository
    @Inject
    lateinit var wakeLock: AlarmWakeLock

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult: PendingResult = goAsync()

        scope.launch(Dispatchers.Default) {
            try {
                val cfg = settingsRepository.alarmCfgFlow.first()
                val msg = settingsRepository.msgFlow.first()
                if(cfg.isStarted){
                    sendSMS(context, msg.phoneNumber1, msg.text)
                    sendSMS(context, msg.phoneNumber2, msg.text)
                }
                Timber.d("MsgReceiver onReceive")
            } finally {
                wakeLock.setWakeLock(false)
                settingsRepository.setStarted(false)
                pendingResult.finish()
            }
        }
    }

    fun sendSMS(context: Context?, phoneNumber: String, msg: String) {
        if(context == null) return
        if(phoneNumber.length == 0) return
        try {
            val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(
                    SmsManager::class.java
                )
            } else {
                SmsManager.getDefault()
            }
            val parts: ArrayList<String> = smsManager.divideMessage(msg)
            smsManager.sendMultipartTextMessage(
                "+" + phoneNumber,
                null,
                parts,
                null,
                null
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}