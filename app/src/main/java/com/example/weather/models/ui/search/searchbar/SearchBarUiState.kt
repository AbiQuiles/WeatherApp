package com.example.weather.models.ui.search.searchbar

import com.example.weather.widgets.BasicUiStates

data class SearchBarUiState(
    override val isLoading: Boolean = false,
    override val isOffline: Boolean = false,
    override val isError: Boolean = false,
    val searchText: String = ""
): BasicUiStates