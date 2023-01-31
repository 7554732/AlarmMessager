package com.fomichev.alarmmessager

import com.fomichev.alarmmessager.AlarmMessagerApplication.Companion.DIVISOR_MIN_IN_HOUR
import com.fomichev.alarmmessager.AlarmMessagerApplication.Companion.DIVISOR_ML_SEC
import com.fomichev.alarmmessager.AlarmMessagerApplication.Companion.DIVISOR_SEC_IN_MIN
import java.text.SimpleDateFormat
import java.util.*

fun timeToString(time: Long, pattern: String): String? {
    val date = Date(time)
    val dateFormat = SimpleDateFormat(pattern)
    val resString = dateFormat.format(date)
    dateFormat.format(date)
    return resString
}

fun Hours(time: Long): Long {
    return time / (DIVISOR_MIN_IN_HOUR * DIVISOR_SEC_IN_MIN * DIVISOR_ML_SEC)
}

fun Minutes(time: Long): Long {
    return (time / (DIVISOR_SEC_IN_MIN * DIVISOR_ML_SEC) - Hours(time) * DIVISOR_MIN_IN_HOUR)
}

fun Seconds(time: Long): Long {
    return (
                time / DIVISOR_ML_SEC
                - Hours(time) * DIVISOR_MIN_IN_HOUR * DIVISOR_SEC_IN_MIN
                - Minutes(time) * DIVISOR_SEC_IN_MIN
            )
}