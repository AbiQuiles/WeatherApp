package com.example.weather.models.ui.search.searchbar

data class SearchBarEvents(
    val searchTextChange: (String) -> Unit,
    val onSearch: (String) -> Unit,
    val isLoading: (Boolean) -> Unit,
    val onExitRoute: () -> Unit = {}
)
