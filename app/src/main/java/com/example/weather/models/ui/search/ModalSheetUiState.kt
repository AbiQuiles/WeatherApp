package com.example.weather.models.ui.search

data class ModalSheetUiState(
    val locationSelected: String = "",
    val showSheet: Boolean = false,
    val isLocationSaved: Boolean = false
)
