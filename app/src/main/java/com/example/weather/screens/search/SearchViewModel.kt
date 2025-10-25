package com.example.weather.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.ui.search.ModalSheetUiState
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

    private val _modalSheetUiState: MutableStateFlow<ModalSheetUiState> = MutableStateFlow(ModalSheetUiState())
    val modalSheetUiState: StateFlow<ModalSheetUiState> = _modalSheetUiState.asStateFlow()

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
                    searchList.copy(items = _cachedSavedLocations)
                }
            }

            launch(Dispatchers.IO) {
                supportedRepository.getAllLocationSupported()
                    .collect { searchItemUiStates ->
                        _cachedSupportedLocations.clear()
                        _cachedSupportedLocations.addAll(searchItemUiStates)
                    }
            }
        }
    }

    fun onSearch(query: String) {
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

    fun onLocationSelected(location: String, isSaved: Boolean) {
        _modalSheetUiState.update { uiState ->
            uiState.copy(
                locationSelected = location,
                isLocationSaved = isSaved,
                showSheet = true
            )
        }
    }

    fun onDismissBottomSheet() = _modalSheetUiState.update { uiState ->
        uiState.copy(showSheet = false)
    }

    fun onLocationSaveStatusChanged(onSave: Boolean) {
        _modalSheetUiState.update { uiState ->
            uiState.copy(isLocationSaved = onSave)
        }

        clearSearch()
    }


    fun setSearchBarText(text: String) = _searchBarUiState.update { uiState ->
        uiState.copy(
            searchText = text
        )
    }

    fun setSearchBarLoadingState(newState: Boolean) = _searchBarUiState.update { uiState ->
        uiState.copy(isLoading = newState)
    }

    private fun clearSearch() {
        setSearchBarText(text = "")
        onSearch(query = "")
    }
}
