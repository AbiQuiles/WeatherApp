package com.example.weather.screens.main

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
fun WeatherScreenTopBar(
    title: String = "",
    onExitRoute: () -> Unit = {},
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
        colors =  TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherModalTopBar(customActions: @Composable () -> Unit = {}) {
    TopAppBar(
        title = {},
        colors =  TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
        actions = { customActions() }
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarMainPreview() {
    WeatherScreenTopBar(
        title = "Main",
        onExitRoute = {},
        isMainScreen = true
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    WeatherScreenTopBar(
        title = "Not Main",
        onExitRoute = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun WeatherModalTopBarPreview() {
    WeatherModalTopBar()
}