package com.example.weather.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.converters.WeatherModelsConverter
import com.example.weather.models.ui.location.SearchResultListUiState
import com.example.weather.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherSearchViewModel @Inject constructor(
    private val repository: LocationRepository,
    private val converter: WeatherModelsConverter
) : ViewModel() {
    private val _cachedLocationList = mutableListOf<String>()

    private val _searchResultListUiState: MutableStateFlow<SearchResultListUiState> = MutableStateFlow(SearchResultListUiState())
    val searchResultListUiState: StateFlow<SearchResultListUiState> = _searchResultListUiState.asStateFlow()

    init {
        viewModelScope.launch {
            _cachedLocationList.addAll(repository._mockLocations)

            _searchResultListUiState.update { searchList ->
                searchList.copy(items = converter.locationEntityToSearchResultItemUiState(_cachedLocationList))
            }
        }
    }

    fun getSearch(query: String) {
        val result = if (query.isNotBlank()) {
            converter.locationEntityToSearchResultItemUiState(
                _cachedLocationList.filter { location ->
                    location.contains(query, true)
                }
            )
        } else {
            converter.locationEntityToSearchResultItemUiState(_cachedLocationList)
        }

        _searchResultListUiState.update { searchList ->
            searchList.copy(items = result)
        }
    }
}
