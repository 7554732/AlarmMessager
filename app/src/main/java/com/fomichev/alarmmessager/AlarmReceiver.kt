package com.fomichev.alarmmessager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "onReceive");
        Log.d("AlarmReceiver", "time = " + intent?.getLongExtra("time",  0L));
    }
}