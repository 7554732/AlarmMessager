package com.fomichev.alarmmessager.domain

data class AlarmCFG (
    val timeToAlarm: Int = 30,
    val timeToMsg: Int = 10,
    val startTime: Long = 0L,
    val isStarted: Boolean = false
)