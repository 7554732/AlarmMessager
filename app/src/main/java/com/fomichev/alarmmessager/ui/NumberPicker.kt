package com.fomichev.alarmmessager.ui

import android.widget.NumberPicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    value: Int = 10,
    min: Int = 0,
    max: Int = 60,
    onSelected: (value: Int) -> Unit
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            NumberPicker(context)
                .apply {
                    setOnValueChangedListener { numberPicker, oldValue, newValue ->
                        onSelected(newValue)
                    }
                    minValue = min
                    maxValue = max
                    setValue(value)
                }
        }
    )
}