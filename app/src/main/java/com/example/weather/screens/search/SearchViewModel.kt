package com.example.weather.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.ui.search.ModalSheetUiState
import com.example.weather.models.ui.search.SavedItemUiState
import com.example.weather.models.ui.search.SearchItemUiState
import com.example.weather.models.ui.search.SearchListItem
import com.example.weather.models.ui.search.SearchListUiState
import com.example.weather.models.ui.search.searchbar.SearchBarUiState
import com.example.weather.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _searchBarUiState: MutableStateFlow<SearchBarUiState> = MutableStateFlow(SearchBarUiState())
    val searchBarUiState: StateFlow<SearchBarUiState> = _searchBarUiState.asStateFlow()

    private val _modalSheetUiState: MutableStateFlow<ModalSheetUiState> = MutableStateFlow(ModalSheetUiState())
    val modalSheetUiState: StateFlow<ModalSheetUiState> = _modalSheetUiState.asStateFlow()

    private val _savedLocations: MutableStateFlow<Set<SavedItemUiState>> = MutableStateFlow(emptySet())
    private val _supportedLocations: MutableStateFlow<Set<SearchItemUiState>> = MutableStateFlow(emptySet())

    val searchListUiState: StateFlow<SearchListUiState> = combine(
        _searchBarUiState,
        _savedLocations,
        _supportedLocations
    ) { searchBarUiState, saveUiStates, supportedUiStates ->

        val items: Set<SearchListItem> = if (searchBarUiState.searchText.isNotBlank()) {
            supportedUiStates.filter { supportedLocation ->
                supportedLocation.name.startsWith(searchBarUiState.searchText, ignoreCase = true)
            }.toSet()
        } else {
            saveUiStates
        }

        SearchListUiState(items = items)
    }.stateIn( // Convert the resulting Flow into a StateFlow for the UI
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // tell the collector to stop collecting(from the db and search text) after 5sec when the user navigates away
        initialValue = SearchListUiState()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.getAllLocations()
                .distinctUntilChanged()
                .collect { savedItems ->
                    _savedLocations.value = savedItems.toSet()
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.getAllLocationSupported()
                .collect { supportedItems ->
                    _supportedLocations.value = supportedItems.toSet()
                }
        }
    }

    fun onSearch(query: String) {
        _searchBarUiState.update { uiState ->
            uiState.copy(searchText = query)
        }

        setSearchBarLoadingState(false)
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

    fun onDeleteLocation(location: String, deleteSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            val deleteSuccess: Boolean = locationRepository.deleteLocation(location = location)
            deleteSuccess(deleteSuccess)
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
