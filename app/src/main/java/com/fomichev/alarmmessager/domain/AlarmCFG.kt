package com.fomichev.alarmmessager.domain

data class AlarmCFG (
    val timeToAlarm: Int = 30,
    val timeToMsg: Int = 10,
    val isStarted: Boolean = false
)