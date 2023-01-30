package com.fomichev.alarmmessager

import android.app.Application
import android.content.Context

import dagger.hilt.android.HiltAndroidApp;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class AlarmMessagerApplication: Application() {

    companion object{
        const val DIVISOR_ML_SEC = 1000
        const val DIVISOR_SEC_IN_MIN = 60
        const val DIVISOR_MIN_IN_HOUR = 60
    }

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        delayedInit()
    }
    private fun delayedInit() {
        if (BuildConfig.DEBUG) {
            applicationScope.launch {
                Timber.plant(Timber.DebugTree())
            }
        }
    }
}
