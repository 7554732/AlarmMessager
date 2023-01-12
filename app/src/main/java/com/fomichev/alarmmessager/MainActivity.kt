package com.fomichev.alarmmessager

import android.R.attr.action
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fomichev.alarmmessager.ui.theme.AlarmMessagerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlarmMessagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting {
                        startAlarm()
                    }
                }
            }
        }
    }

    private fun startAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("time", 100L)
        val pIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        sendBroadcast(intent)
    }
}

@Composable
fun Greeting(onStart: () -> Unit) {
    Button(
        onClick = onStart
    ){
        Text(text = "Stat")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AlarmMessagerTheme {
        Greeting({})
    }
}