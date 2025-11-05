package com.example.weather.models.ui.search

import java.util.UUID

data class SearchItemUiState(
    override val id: UUID,
    val name: String = "Nothing found",
    val saveTag: Boolean = false,
): SearchListItem
