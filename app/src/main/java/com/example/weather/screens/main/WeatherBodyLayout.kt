package com.example.weather.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState

@Composable
fun WeatherBodyLayout(
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
                    lowAndHighTemp = currentWeatherUiState.lowAndHighTemp
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
    lowAndHighTemp: Pair<String, String>,
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
                text = "Low ${lowAndHighTemp.first} | High ${lowAndHighTemp.second}",
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

@Preview(showBackground = true)
@Composable
private fun CurrentWeatherPreview() {
    val currentWeatherUiState = CurrentWeatherUiState()

    CurrentWeather(
        location = currentWeatherUiState.city,
        currentTemp = currentWeatherUiState.currentTemp,
        tempDescription = currentWeatherUiState.tempDescription,
        feelsLike = currentWeatherUiState.feelsLike,
        lowAndHighTemp = currentWeatherUiState.lowAndHighTemp

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