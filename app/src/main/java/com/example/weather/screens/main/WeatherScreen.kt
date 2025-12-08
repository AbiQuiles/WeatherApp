package com.example.weather.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import com.example.weather.navigation.AppRoutes
import com.example.weather.screens.location.permission.LocationPermissionPrompt

@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val currentWeatherUiState by viewModel.currentWeatherUiState.collectAsState()
    val dailyForecastItemUiState by viewModel.dailyForecastItemUiState.collectAsState()
    val loadingState by remember(currentWeatherUiState) {
        if (currentWeatherUiState == null)
            mutableStateOf(true)
        else
            mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopBar(navController = navController)
        }
    ) { innerPadding ->
        LocationPermissionPrompt {
            WeatherBodyLayout(
                currentWeatherUiState = currentWeatherUiState,
                dailyForecastItemUiState = dailyForecastItemUiState,
                loadingState = loadingState,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun TopBar(navController: NavController) {
    WeatherScreenTopBar(
        title = "Weather",
        isMainScreen = true,
        customActions = {
            SearchButton(
                navigate = {
                    navController.navigate(
                        route = AppRoutes.SearchScreen.name
                    )
                }
            )
        }
    )
}

@Composable
private fun SearchButton(navigate: () -> Unit) {
    IconButton(
        onClick = {
            navigate()
        }) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ManageSearch,
            contentDescription = "Search Icon",
            modifier = Modifier.size(30.dp)
        )
    }
}

/**Preview Functions **/
@Preview
@Composable
private fun TopAppBarPreview() {
    val fakeNaveController = NavHostController(LocalContext.current)
    TopBar(navController = fakeNaveController)
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