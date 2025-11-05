package com.example.weather.models.ui.search

import java.util.UUID

data class SavedItemUiState(
    override val id: UUID,
    val name: String = "Orlando",
    val descriptionTemp: String = "Sunny",
    val currentTemp: String = "80",
    val lowAndHighTemp: Pair<String, String> = Pair("-200", "200"),
): SearchListItem
