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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.weather.models.ui.WeatherMainUiState
import com.example.weather.navigation.AppRoutes
import com.example.weather.widgets.TopBar

@Composable
fun WeatherMainScreen(navController: NavController, viewModel: WeatherMainViewModel = hiltViewModel()) {
   /* val weatherData = produceState(
        initialValue = DataOrException(loading = true)
    ) {
        value = viewModel.getWeather(city = "Orlando")
    }.value*/
    val weatherUiState by viewModel.weatherUiState.collectAsState()
    val loadingState by remember(weatherUiState) {
        if (weatherUiState == null)
            mutableStateOf(true)
        else
            mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onSearchRoute = { navController.navigate(route = AppRoutes.SearchScreen.name) }
            )
        }
    ) { innerPadding ->
            MainLayout(
                weatherUiState = weatherUiState,
                loadingState = loadingState,
                modifier = Modifier.padding(innerPadding)
            )
    }
}

@Composable
private fun TopAppBar(onSearchRoute: () -> Unit) {
    TopBar(
        title = "Weather",
        onExitRoute = { },
        isMainScreen = true,
        customActions = {
            IconButton(onClick = onSearchRoute) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(30.dp)
                )
            }
            MinimalDropdownMenu()
        }
    )
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
                        text = "Edit",
                        fontSize = 18.sp,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Icon",
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
private fun MainLayout(
    weatherUiState: WeatherMainUiState?,
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
                if (!loadingState && weatherUiState != null) {
                    CurrentWeather(
                        location = weatherUiState.city,
                        currentTemp = weatherUiState.currentTemp,
                        tempDescription = weatherUiState.tempDescription,
                        feelsLike = weatherUiState.feelsLike,
                        highAndLowTemp = weatherUiState.highAndLowTemp
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    /*WeekForecastCard(
                        dayForecastImage = weatherEntity?.list?.first()?.weather?.first()?.icon ?: ""
                    )*/
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
            fontSize = 55.sp,
        )
        Text(
            text = currentTemp,
            fontSize = 90.sp,
            fontWeight = FontWeight.W300
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
                text = "Heigh ${highAndLowTemp.first} | Low ${highAndLowTemp.second}",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun WeekForecastCard(
    dayOfWeek: String = "Today",
    dayForecastImage: String = "",
    highAndLow: Pair<String, String> = Pair("89°", "75°")
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFB7D9F5),
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

            for(i in 1..14) {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(6.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = dayOfWeek,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    CurrentWeatherImage(weatherImage = dayForecastImage)

                    Text(
                        text = "Low ${highAndLow.second} |  High ${highAndLow.first}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrentWeatherImage(weatherImage: String) {
    val imageUrl = "https://openweathermap.org/img/wn/${weatherImage}.png"
    val painter = rememberAsyncImagePainter(imageUrl)

    Image(
        painter = painter,
        contentDescription = "Current Weather Image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.size(45.dp)
    )
}

@Preview
@Composable
private fun TopAppBarPreview() {
    TopAppBar(onSearchRoute = {})
}

@Preview(showBackground = true)
@Composable
private fun MainLayoutPreview() {
    MainLayout(
        weatherUiState = WeatherMainUiState(),
        loadingState = false
    )
}