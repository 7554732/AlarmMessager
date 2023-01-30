package com.fomichev.alarmmessager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import androidx.core.content.ContextCompat.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmWakeLock @Inject constructor(@ApplicationContext val appContext: Context)  {
    private val wakeLock: PowerManager.WakeLock by lazy{
        val powerManager = appContext.getSystemService(POWER_SERVICE) as PowerManager
        powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "Alarm:WakeLockTag"
        )
    }

    fun setWakeLock(lock: Boolean) {
        if (lock) {
            wakeLock.acquire()
        } else {
            if (wakeLock.isHeld()) wakeLock.release()
        }
    }
}