package com.example.weather.models.ui.search

data class SavedItemUiState(
    val name: String = "Orlando",
    val descriptionTemp: String = "Sunny",
    val currentTemp: String = "80",
    val lowAndHighTemp: Pair<String, String> = Pair("-200", "200")
): SearchListItem
