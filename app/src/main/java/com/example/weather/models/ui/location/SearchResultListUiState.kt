package com.example.weather.models.ui.location

import com.example.weather.widgets.BasicUiStates

data class SearchResultListUiState(
    override val isLoading: Boolean = false,
    override val isOffline: Boolean = false,
    override val isError: Boolean = false,
    val items: List<SearchResultItemUiState> = emptyList(),
): BasicUiStates
