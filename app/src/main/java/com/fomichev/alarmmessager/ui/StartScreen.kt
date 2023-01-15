package com.fomichev.alarmmessager.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun StartScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Time to Alarm")
        TimePicker(onTimeSelected = { h,m ->
            Log.d("TimePicker ", "" + h + " " + m)
        })
        Text(text = "Time from Alarm to Message")
        NumberPicker(onSelected = { v ->
            Log.d("NumberPicker ", "" + v)
        })
        Button(
            onClick = onStart
        ) {
            Text(text = "Start")
        }
    }
}