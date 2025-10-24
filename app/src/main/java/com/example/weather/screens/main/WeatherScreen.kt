package com.example.weather.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import com.example.weather.navigation.AppNavKeys
import com.example.weather.navigation.AppRoutes
import com.example.weather.widgets.FloatingModal
import com.example.weather.widgets.SwitcherRow
import com.example.weather.widgets.WeatherScreenTopBar

@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel(),
    locationName: String?
) {
    val currentWeatherUiState by viewModel.currentWeatherUiState.collectAsState()
    val dailyForecastItemUiState by viewModel.dailyForecastItemUiState.collectAsState()
    val loadingState by remember(currentWeatherUiState) {
        if (currentWeatherUiState == null)
            mutableStateOf(true)
        else
            mutableStateOf(false)
    }

    LaunchedEffect(locationName) {
        if (locationName != null) {
            viewModel.getSelectedWeather(locationName)

            //Clear value from the savedStateHandle
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>(AppNavKeys.LOCATION_NAME)
        }
    }

    Scaffold(
        topBar = {
            TopBar(navController = navController)
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
            //SettingsBottomSheet()
            //MinimalDropdownMenu()
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

@Composable
private fun MinimalDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .padding(horizontal = 8.dp)
        .size(28.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More options",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxSize()
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.align(Alignment.TopEnd)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Permissions",
                        fontSize = 18.sp,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings Icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {

                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Delete",
                        color = Color.Red,
                        fontSize = 18.sp,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Icon",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {

                }
            )
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SettingsBottomSheet() {
    var showBottomSheetState by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = { showBottomSheetState = true }
    ) {
        Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = "Settings Icon",
            modifier = Modifier.size(25.dp)
        )
    }

    if (showBottomSheetState) {
        FloatingModal(showBottomSheetState = { showBottomSheetState = false }) {
            Column {
                Text(
                    text = "Permissions",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                SwitcherRow(
                    icon = Icons.Default.Notifications,
                    text = "Notification"
                ) {

                }
                SwitcherRow(
                    icon = Icons.Default.LocationOn,
                    text = "Location"
                ) {

                }
            }
        }
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