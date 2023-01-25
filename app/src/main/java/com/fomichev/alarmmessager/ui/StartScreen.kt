package com.fomichev.alarmmessager.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomichev.alarmmessager.R
import com.fomichev.alarmmessager.domain.AlarmCFG
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StartScreen(
    alarmCfg: AlarmCFG?,
    alarmCfgInit: AlarmCFG,
    onStart: (AlarmCFG) -> Unit
) {
    var timeToAlarm by rememberSaveable { mutableStateOf(alarmCfgInit.timeToAlarm) }
    var timeToMsg by rememberSaveable { mutableStateOf(alarmCfgInit.timeToMsg) }
    var startTime by rememberSaveable { mutableStateOf(alarmCfgInit.startTime) }
    var isStarted by rememberSaveable { mutableStateOf(alarmCfgInit.isStarted) }

//    update states when alarmCfg change
    var isInit by rememberSaveable(
        inputs = arrayOf(alarmCfg),
        init = {
            alarmCfg?.let {
                timeToAlarm = it.timeToAlarm
                timeToMsg = it.timeToMsg
                startTime = it.startTime
                isStarted = it.isStarted
            }
            mutableStateOf(true)
        }
    )
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
                    hour =  timeToAlarm / 60,
                    minute = timeToAlarm % 60,
                    onTimeSelected = { h,m ->
                        timeToAlarm = h * 60 + m
                    }
                )
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
                    value = timeToMsg,
                    onSelected = { v ->
                        timeToMsg = v
                    }
                )
            }
        }
        CircleStartButton(
            isStarted = isStarted,
            onStart = {
                isStarted = it
                onStart(AlarmCFG(timeToAlarm, timeToMsg, System.currentTimeMillis(), isStarted))
            }
        )
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            text = if(isStarted) "" + timeToString(startTime + (timeToAlarm + timeToMsg) * 1000 * 60) else "",
            textAlign = TextAlign.Center
        )
    }
}

private fun timeToString(time: Long): String? {
    val date = Date(time)
    val dateFormat = SimpleDateFormat("y-M-d H:m:s")
    val resString = dateFormat.format(date)
    dateFormat.format(date)
    return resString
}

@Composable
fun CircleStartButton(
    modifier: Modifier = Modifier,
    isStarted: Boolean = false,
    onStart: (Boolean) -> Unit
){
    Button(
        modifier = modifier.size(150.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        onClick = {
            onStart(!isStarted)
        }
    ) {
        Text(
            text = if(!isStarted)
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