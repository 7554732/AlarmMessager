package com.fomichev.alarmmessager.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
        CircleStartButton(onStart = {})
    }
}


@Composable
fun CircleStartButton(
    modifier: Modifier = Modifier,
    isStartedState: MutableState<Boolean> = remember { mutableStateOf(false) },
    onStart: (Boolean) -> Unit
){
    Button(
        modifier = modifier.size(150.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        onClick = {
            isStartedState.value = !isStartedState.value
            onStart(isStartedState.value)
        }
    ) {
        Text(
            text = if(!isStartedState.value)
                stringResource(R.string.start)
            else
                stringResource(R.string.stop) ,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun Preview() {
    CircleStartButton(onStart = {})
}