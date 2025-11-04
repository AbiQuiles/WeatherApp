package com.example.weather.models.ui.search

data class SavedItemEvents(
    val onLeadingSwipe: (String) -> Unit,
    val onTrailingSwipe: (String) -> Unit,
)
