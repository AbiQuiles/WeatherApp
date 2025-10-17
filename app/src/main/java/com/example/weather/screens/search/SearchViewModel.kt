package com.example.weather.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.converters.WeatherModelsConverter
import com.example.weather.models.data.location.LocationSavedEntity
import com.example.weather.models.data.location.LocationSupportedEntity
import com.example.weather.models.ui.search.SearchListUiState
import com.example.weather.models.ui.search.searchbar.SearchBarUiState
import com.example.weather.repository.LocationSavedRepository
import com.example.weather.repository.LocationSupportedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedRepository: LocationSavedRepository,
    private val supportedRepository: LocationSupportedRepository,
    private val converter: WeatherModelsConverter
) : ViewModel() {
    private val _cachedSavedLocations: MutableList<LocationSavedEntity> = mutableListOf()
    private val _cachedSupportedLocations: MutableList<LocationSupportedEntity> = mutableListOf()

    private val _searchBarUiState: MutableStateFlow<SearchBarUiState> = MutableStateFlow(SearchBarUiState())
    val searchBarUiState: StateFlow<SearchBarUiState> = _searchBarUiState.asStateFlow()

    private val _searchListUiState: MutableStateFlow<SearchListUiState> = MutableStateFlow(SearchListUiState())
    val searchListUiState: StateFlow<SearchListUiState> = _searchListUiState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                _cachedSavedLocations.addAll(savedRepository._mockLocationSaved)

                _searchListUiState.update { searchList ->
                    searchList.copy(
                        items = converter.savedEntityToSavedItemUiState(_cachedSavedLocations)
                    )
                }
            }

            launch {
                supportedRepository.getAllLocationSupported().collect { entities ->
                    _cachedSupportedLocations.addAll(entities)
                }
            }
        }
    }

    /*private fun logSupportedLocations() {
        _cachedSupportedLocations.forEach {
            println("Rez json ${it.name}")
        }
    }*/

    fun getSearch(query: String) {
        val result = if(query.isNotBlank()) {
            converter.supportedEntityToSearchItemUiState(
                _cachedSupportedLocations.filter { supportedEntity ->
                    supportedEntity.name.startsWith(query, true)
                }
            )
        } else {
            converter.savedEntityToSavedItemUiState(_cachedSavedLocations)
        }

        setSearchBarLoadingState(false)

        _searchListUiState.update { searchList ->
            searchList.copy(items = result)
        }
    }

    fun setSearchBarText(text: String) = _searchBarUiState.update { uiState ->
        uiState.copy(
            searchText = text
        )
    }

    fun setSearchBarLoadingState(newState: Boolean) = _searchBarUiState.update { uiState ->
        uiState.copy(isLoading = newState)
    }
}
