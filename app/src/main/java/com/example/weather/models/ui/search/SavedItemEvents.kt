package com.example.weather.models.ui.search

data class SavedItemEvents(
    val onClick: (SavedItemUiState) -> Unit,
    val onLeadingSwipe: (SavedItemUiState) -> Unit,
    val onTrailingSwipe: (SavedItemUiState) -> Unit,
)
