package com.example.weather.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import com.example.weather.widgets.rememberSnackbarManager

@Composable
fun WeatherModalScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    locationName: String,
    locationSaved: Boolean,
    isLocationSaved: (Boolean) -> Unit
) {
    val currentWeatherUiState: CurrentWeatherUiState? by viewModel.currentWeatherUiState.collectAsState()
    val dailyForecastItemUiState: List<DailyForecastItemUiState?> by viewModel.dailyForecastItemUiState.collectAsState()
    val loadingState by remember(currentWeatherUiState) {
        mutableStateOf(currentWeatherUiState == null)
    }
    val snackbarManager = rememberSnackbarManager()

    LaunchedEffect(locationName) {
        viewModel.getSelectedWeather(locationName)
    }

    Scaffold(
        topBar = {
            TopBar(locationSaved = locationSaved) { onSave ->
                viewModel.saveLocationByName { saveSuccess ->
                    if (saveSuccess) {
                        isLocationSaved(onSave)
                        snackbarManager.showSnackbar("Location save successful")
                    } else {
                        snackbarManager.showSnackbar("Failed to save location")
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarManager.snackbarHostState)
        }
    ) { innerPadding ->
        WeatherBodyLayout(
            currentWeatherUiState = currentWeatherUiState,
            dailyForecastItemUiState = dailyForecastItemUiState,
            loadingState = loadingState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun TopBar(locationSaved: Boolean, onSave: (Boolean) -> Unit) {
    WeatherModalTopBar(
        customActions = {
            if (!locationSaved) {
                SaveButton {
                    onSave(it)
                }
            }
        }
    )
}

@Composable
private fun SaveButton(onSave: (Boolean) -> Unit) {
    IconButton(
        onClick = { onSave(true) },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Saved Icon",
            modifier = Modifier.size(25.dp)
        )
    }
}

/**Preview Functions **/

@Preview(showBackground = true)
@Composable
private fun ModalTopAppBarPreview() {
    TopBar(
        locationSaved = false,
        onSave = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun WeatherBodyLayoutPreview() {
    val currentWeatherUiState = CurrentWeatherUiState()
    val dailyForecastItemUiState = DailyForecastItemUiState()

    WeatherBodyLayout(
        currentWeatherUiState = currentWeatherUiState,
        dailyForecastItemUiState = listOf(
            dailyForecastItemUiState,
            dailyForecastItemUiState,
            dailyForecastItemUiState,
            dailyForecastItemUiState,
            dailyForecastItemUiState,
            dailyForecastItemUiState
        ),
        loadingState = false,
    )
}