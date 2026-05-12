package com.nyoike.ventpulse.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> ObserveAsEvents(
    events: Flow<T>,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(events, lifecycleOwner.lifecycle) {
        events
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect(onEvent)
    }
}
