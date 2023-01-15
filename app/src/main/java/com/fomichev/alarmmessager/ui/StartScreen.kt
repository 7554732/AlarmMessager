package com.fomichev.alarmmessager.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fomichev.alarmmessager.R

@Composable
fun StartScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(){
            Column(
                modifier = Modifier.weight(0.5F),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.time_to_alarm),
                    textAlign = TextAlign.Center
                )
                TimePicker(
                    modifier = Modifier.padding(16.dp),
                    onTimeSelected = { h,m ->
                    Log.d("TimePicker ", "" + h + " " + m)
                })
            }
            Column(
                modifier = Modifier.weight(0.5F),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.time_to_msg),
                    textAlign = TextAlign.Center
                )
                NumberPicker(
                    modifier = Modifier.padding(16.dp),
                    onSelected = { v ->
                    Log.d("NumberPicker ", "" + v)
                })
            }
        }
        Button(
            onClick = onStart
        ) {
            Text(text = stringResource(R.string.start))
        }
    }
}