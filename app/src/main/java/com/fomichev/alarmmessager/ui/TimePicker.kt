package com.fomichev.alarmmessager.ui

import android.os.Build
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.fomichev.alarmmessager.databinding.TimePickerBinding

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    hour: Int = 0,
    minute: Int = 30,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            val timePicker = TimePickerBinding.inflate(LayoutInflater.from(context)).timePicker

            timePicker.setIs24HourView(true)
            timePicker.setTime(hour, minute)

            timePicker.setOnTimeChangedListener({ view, hour, minute ->
                onTimeSelected(hour, minute)
            })

            timePicker
        }
    )
}

fun TimePicker.setTime(hour: Int, minute: Int){
    val timePicker = this
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        timePicker.hour = hour
        timePicker.minute = minute
    } else {
        timePicker.setCurrentHour(hour)
        timePicker.setCurrentMinute(minute)
    }
}
