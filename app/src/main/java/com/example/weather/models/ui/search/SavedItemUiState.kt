package com.example.weather.models.ui.search

data class SavedItemUiState(
    val name: String = "Orlando",
    val highAndLowTemp: Pair<String, String> = Pair("200°", "-200°")
): SearchListItem
