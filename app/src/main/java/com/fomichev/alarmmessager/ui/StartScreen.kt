package com.fomichev.alarmmessager.ui

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
import com.fomichev.alarmmessager.domain.AlarmCFG
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.fomichev.alarmmessager.*
import com.fomichev.alarmmessager.AlarmMessagerApplication.Companion.DIVISOR_MIN_IN_HOUR
import com.fomichev.alarmmessager.AlarmMessagerApplication.Companion.DIVISOR_ML_SEC
import com.fomichev.alarmmessager.AlarmMessagerApplication.Companion.DIVISOR_SEC_IN_MIN
import com.fomichev.alarmmessager.R
import java.util.*

@Composable
fun StartScreen(
    alarmCfg: AlarmCFG,
    timeLeftToMsg: Long?,
    onStart: (AlarmCFG) -> Unit
) {
    var timeToAlarm by rememberSaveable { mutableStateOf(alarmCfg.timeToAlarm) }
    var timeToMsg by rememberSaveable { mutableStateOf(alarmCfg.timeToMsg) }
    var startTime by rememberSaveable { mutableStateOf(alarmCfg.startTime) }
    var isStarted by rememberSaveable { mutableStateOf(alarmCfg.isStarted) }

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
                    hour =  timeToAlarm / DIVISOR_MIN_IN_HOUR,
                    minute = timeToAlarm % DIVISOR_MIN_IN_HOUR,
                    onTimeSelected = { h,m ->
                        timeToAlarm = h * DIVISOR_MIN_IN_HOUR + m
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
            text = if(isStarted)
                    "" + timeToString(
                        startTime + (timeToAlarm + timeToMsg) * DIVISOR_SEC_IN_MIN * DIVISOR_ML_SEC,
                        "y-M-d H:m:s"
                    )
                    + " " + timeLeftToMsg?.let {
                        Hours(it).toString() + ":" +
                        Minutes(it).toString() + ":" +
                        Seconds(it).toString()
                    }
                else "",
            textAlign = TextAlign.Center
        )
    }
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