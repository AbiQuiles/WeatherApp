package com.example.weather.models.mappers

import com.example.weather.models.data.location.LocationSavedEntity
import com.example.weather.models.data.location.LocationSupportedDto
import com.example.weather.models.data.location.LocationSupportedEntity
import com.example.weather.models.ui.search.SavedItemUiState
import com.example.weather.models.ui.search.SearchItemUiState

fun LocationSavedEntity.toUiModel(): SavedItemUiState = SavedItemUiState(
    name = this.name,
    descriptionTemp = this.descriptionTemp,
    currentTemp = this.currentTemp,
    lowAndHighTemp = Pair(this.minTemp, this.maxTemp)
)

fun LocationSupportedEntity.toUiModel(): SearchItemUiState = SearchItemUiState(
    name = this.name,
    saveTag = this.saveTag
)

fun LocationSupportedDto.toEntity(): LocationSupportedEntity = LocationSupportedEntity(
    name = this.name
)