package com.example.weather.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weather.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    title: String = "",
    onExitRoute: () -> Unit,
    isMainScreen: Boolean = false,
    customActions: @Composable () -> Unit = {},
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isMainScreen) {
                    Image(
                        painter = painterResource(R.drawable.weather_splash),
                        contentDescription = "",
                        modifier = Modifier
                            .size(45.dp)
                    )
                }
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        },
        navigationIcon = {
            if (!isMainScreen) {
                IconButton(onClick = onExitRoute) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back icon"
                    )
                }
            }
        },
        actions = { customActions() }
    )
}

@Preview
@Composable
private fun TopAppBarMainPreview() {
    TopBar(
        title = "Main",
        onExitRoute = {},
        isMainScreen = true
    )
}

@Preview
@Composable
private fun TopAppBarPreview() {
    TopBar(
        title = "Not Main",
        onExitRoute = {},
    )
}

