package com.example.weather.models.ui.search

import com.example.weather.widgets.BasicUiStates

data class SearchListUiState(
    override val isLoading: Boolean = false,
    override val isOffline: Boolean = false,
    override val isError: Boolean = false,
    val items: List<SearchListItem> = emptyList(),
): BasicUiStates
