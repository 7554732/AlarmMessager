package com.fomichev.alarmmessager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fomichev.alarmmessager.R
import com.fomichev.alarmmessager.domain.Msg

@Composable
fun ConfigScreen(
    msg: Msg,
    onSave: (Msg) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var phoneNumber by rememberSaveable { mutableStateOf(msg.phoneNumber) }
        PhoneField(phoneNumber,
            modifier = Modifier.padding(16.dp),
            mask = "+7-000-000-00-00",
            maskNumber = '0',
            onPhoneChanged = { phoneNumber = it })

        var msgText by rememberSaveable { mutableStateOf(msg.text) }
        MsgTextField(
            msgText,
            Modifier
                .padding(16.dp)
                .height(150.dp),
            onMsgChanged = { msgText = it }
        )

        Button(onClick = { onSave(Msg(phoneNumber, msgText)) }) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
fun MsgTextField(
    msg: String,
    modifier: Modifier = Modifier,
    onMsgChanged: (String) -> Unit
) {
    TextField(
        value = msg,
        onValueChange = { onMsgChanged(it) },
        label = { Text(stringResource(R.string.entr_msg)) },
        modifier = modifier.fillMaxWidth(),
    )
}

