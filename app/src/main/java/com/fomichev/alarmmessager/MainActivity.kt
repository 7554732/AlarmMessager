package com.fomichev.alarmmessager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.fomichev.alarmmessager.ui.MainPagerScreen
import com.fomichev.alarmmessager.ui.theme.AlarmMessagerTheme


class MainActivity : ComponentActivity() {
    val PENDING_INTENT_FLAG_IMMUTABLE =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlarmMessagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainPagerScreen (
                        onStart = {startAlarm()},
                        onConfigSave = { msg ->
                            Log.d("MainActivity ", "" + msg)
                        }
                    )
                }
            }
        }
    }

    private fun startAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("time", 100L)
        val pIntent = PendingIntent.getBroadcast(this, 0, intent, PENDING_INTENT_FLAG_IMMUTABLE)
        val am = getSystemService(ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 4000, pIntent)
    }
}
