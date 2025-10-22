package com.example.weather.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.ui.search.SavedItemUiState
import com.example.weather.models.ui.search.SearchItemUiState
import com.example.weather.models.ui.search.SearchListItem
import com.example.weather.models.ui.search.SearchListUiState
import com.example.weather.models.ui.search.searchbar.SearchBarUiState
import com.example.weather.repository.LocationSavedRepository
import com.example.weather.repository.LocationSupportedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedRepository: LocationSavedRepository,
    private val supportedRepository: LocationSupportedRepository,
) : ViewModel() {
    private val _cachedSavedLocations: MutableSet<SavedItemUiState> = mutableSetOf()
    private val _cachedSupportedLocations: MutableSet<SearchItemUiState> = mutableSetOf()

    private val _searchBarUiState: MutableStateFlow<SearchBarUiState> = MutableStateFlow(SearchBarUiState())
    val searchBarUiState: StateFlow<SearchBarUiState> = _searchBarUiState.asStateFlow()

    private val _searchListUiState: MutableStateFlow<SearchListUiState> = MutableStateFlow(SearchListUiState())
    val searchListUiState: StateFlow<SearchListUiState> = _searchListUiState.asStateFlow()

    init {
        viewModelScope.launch {

            launch(Dispatchers.IO) {
                savedRepository.getAllLocations()
                    .distinctUntilChanged()
                    .collect { savedItemUiStates ->
                        _cachedSavedLocations.addAll(savedItemUiStates)
                    }
            }

            launch(Dispatchers.Main) {
                _searchListUiState.update { searchList ->
                    searchList.copy(
                        items = _cachedSavedLocations
                    )
                }
            }

            launch(Dispatchers.IO) {
                supportedRepository.getAllLocationSupportedN()
                    .collect { searchItemUiStates ->
                        _cachedSupportedLocations.addAll(searchItemUiStates)
                    }
            }
        }
    }

    /*fun log() {
       _cachedSavedLocations.forEach {
           println("Rez saved ${it}")
       }
   }*/

    fun getSearch(query: String) {
        val result: Set<SearchListItem> = if (query.isNotBlank()) {
            _cachedSupportedLocations.filter { supportedEntity ->
                supportedEntity.name.startsWith(query, true)
            }.toSet()
        } else {
            _cachedSavedLocations
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
