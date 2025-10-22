package com.example.weather.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import com.example.weather.navigation.AppNavKeys
import com.example.weather.navigation.AppRoutes
import com.example.weather.widgets.FloatingModal
import com.example.weather.widgets.SwitcherRow
import com.example.weather.widgets.WeatherModalTopBar
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
            WeatherTopBar(navController = navController)
        }
    ) { innerPadding ->
            MainLayout(
                currentWeatherUiState = currentWeatherUiState,
                dailyForecastItemUiState = dailyForecastItemUiState,
                loadingState = loadingState,
                modifier = Modifier.padding(innerPadding)
            )
    }
}

@Composable
fun WeatherModalScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    locationName: String
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
        viewModel.getSelectedWeather(locationName)
    }

    Scaffold(
        topBar = {
            ModalTopBar(
                onSave = { viewModel.saveLocationByName(onSave = it) }
            )
        }
    ) { innerPadding ->
        MainLayout(
            currentWeatherUiState = currentWeatherUiState,
            dailyForecastItemUiState = dailyForecastItemUiState,
            loadingState = loadingState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun WeatherTopBar(navController: NavController) {
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
private fun ModalTopBar(onSave: (Boolean) -> Unit) {
    WeatherModalTopBar(
        customActions = {
            SaveButton {
                onSave(it)
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

@Composable
private fun MainLayout(
    currentWeatherUiState: CurrentWeatherUiState?,
    dailyForecastItemUiState: List<DailyForecastItemUiState?>,
    loadingState: Boolean,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
            item {
                if (!loadingState && currentWeatherUiState != null) {
                    CurrentWeather(
                        location = currentWeatherUiState.city,
                        currentTemp = currentWeatherUiState.currentTemp,
                        tempDescription = currentWeatherUiState.tempDescription,
                        feelsLike = currentWeatherUiState.feelsLike,
                        highAndLowTemp = currentWeatherUiState.highAndLowTemp
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    WeekForecastCard(dailyForecastItemUiState = dailyForecastItemUiState)
                } else {
                    CircularProgressIndicator(modifier = modifier)
                }
            }
    }
}

@Composable
private fun CurrentWeather(
    location: String,
    currentTemp: String,
    tempDescription: String,
    feelsLike: String,
    highAndLowTemp: Pair<String, String>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = location,
            fontSize = 35.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = currentTemp,
            fontSize = 90.sp,
            fontWeight = FontWeight.W300,
        )
        Text(
            text = tempDescription,
            fontSize = 18.sp
        )
        Text(
            text = "Feels Like $feelsLike",
            fontSize = 22.sp
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "High ${highAndLowTemp.first} | Low ${highAndLowTemp.second}",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun WeekForecastCard(dailyForecastItemUiState: List<DailyForecastItemUiState?>) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null
                )
                Text(
                    text = "Week Forecast",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                )
            }

            if (dailyForecastItemUiState.isNotEmpty()) {
                dailyForecastItemUiState.forEach { dailyForecastItem ->
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(6.dp)
                    )

                    if (dailyForecastItem != null) {
                        DailyForecastItem(
                            dayOfWeek = dailyForecastItem.day,
                            forecastUrlImage = dailyForecastItem.forecastUrlImage,
                            highAndLow = dailyForecastItem.highAndLowTemp
                        )
                    } else {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun DailyForecastItem(
    dayOfWeek: String,
    forecastUrlImage: String,
    highAndLow: Pair<String, String>,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)

    ) {
        Text(
            text = dayOfWeek,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        CurrentWeatherImage(forecastUrlImage = forecastUrlImage)

        Text(
            text = "Low ${highAndLow.second} | High ${highAndLow.first}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CurrentWeatherImage(forecastUrlImage: String) {
    val painter = rememberAsyncImagePainter(forecastUrlImage)

    Image(
        painter = painter,
        contentDescription = "Current Weather Image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.size(60.dp)
    )

}

/**Preview Functions **/
@Preview
@Composable
private fun TopAppBarPreview() {
    val fakeNaveController = NavHostController(LocalContext.current)
    WeatherTopBar(navController = fakeNaveController)
}

@Preview(showBackground = true)
@Composable
private fun ModalTopAppBarPreview() {
    ModalTopBar(onSave = {} )
}

@Preview(showBackground = true)
@Composable
private fun MainLayoutPreview() {
    val currentWeatherUiState = CurrentWeatherUiState()
    val dailyForecastItemUiState = DailyForecastItemUiState()

    MainLayout(
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

@Preview(showBackground = true)
@Composable
private fun CurrentWeatherPreview() {
    val currentWeatherUiState = CurrentWeatherUiState()

    CurrentWeather(
        location = currentWeatherUiState.city,
        currentTemp = currentWeatherUiState.currentTemp,
        tempDescription = currentWeatherUiState.tempDescription,
        feelsLike = currentWeatherUiState.feelsLike,
        highAndLowTemp = currentWeatherUiState.highAndLowTemp

    )
}

@Preview(showBackground = true)
@Composable
private fun WeekForecastCardPreview() {
    val dailyForecastItemUiState = DailyForecastItemUiState()

    WeekForecastCard(
        dailyForecastItemUiState = listOf(
            dailyForecastItemUiState,
            dailyForecastItemUiState,
            dailyForecastItemUiState
        )
    )
}