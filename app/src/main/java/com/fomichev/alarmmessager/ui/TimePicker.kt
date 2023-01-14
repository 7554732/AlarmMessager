package com.fomichev.alarmmessager.ui

import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.fomichev.alarmmessager.R

@Composable
fun TimePicker(
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.time_picker, null)
            val timePicker = view.findViewById<TimePicker>(R.id.timePicker)

            timePicker.hour = 0
            timePicker.minute = 30
            timePicker.setIs24HourView(true)

            timePicker.setOnTimeChangedListener({ view, hour, minute ->
                onTimeSelected(hour, minute)
            })

            timePicker
        }
    )
}