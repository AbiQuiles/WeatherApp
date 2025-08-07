package com.example.weather.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SwitcherRow(icon: ImageVector? = null, text: String = "Text", checkedState: (Boolean) -> Unit) {
    var checked by remember(checkedState) {
        mutableStateOf(false)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp, horizontal = 12.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = "Notification Switcher",
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = text,
            fontSize = 18.sp,
        )
        Spacer(Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                checkedState(it)
            },
            modifier = Modifier.scale(0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwitcherRowWithIconPreview() {
    SwitcherRow(
        icon = Icons.Default.Notifications,
        checkedState = { true }
    )
}

@Preview(showBackground = true)
@Composable
private fun SwitcherRowWithoutIconPreview() {
    SwitcherRow(
        checkedState = { true }
    )
}