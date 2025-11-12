package com.example.weather.widgets.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarManager(
    val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    fun showSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }
}

@Composable
fun rememberSnackbarManager(): SnackbarManager {
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    return remember(snackbarHostState, coroutineScope) {
        SnackbarManager(snackbarHostState, coroutineScope)
    }
}