package com.example.weather.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingModal(
    showBottomSheetState: (Boolean) -> Unit,
    component: @Composable () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { showBottomSheetState(false) },
        sheetState = sheetState,
        sheetMaxWidth = 430.dp,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(bottom = 30.dp)
    ) {
        component()
    }
}