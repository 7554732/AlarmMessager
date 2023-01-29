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
