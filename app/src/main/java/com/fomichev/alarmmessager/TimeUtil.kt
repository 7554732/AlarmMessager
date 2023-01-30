package com.fomichev.alarmmessager

import java.text.SimpleDateFormat
import java.util.*

fun timeToString(time: Long): String? {
    val date = Date(time)
    val dateFormat = SimpleDateFormat("y-M-d H:m:s")
    val resString = dateFormat.format(date)
    dateFormat.format(date)
    return resString
}