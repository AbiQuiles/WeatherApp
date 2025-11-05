package com.example.weather.models.ui.search

data class SavedItemEvents(
    val onLeadingSwipe: (SavedItemUiState) -> Unit,
    val onTrailingSwipe: (String) -> Unit,
)
