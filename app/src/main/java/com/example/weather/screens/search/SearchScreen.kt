package com.example.weather.screens.search

 import androidx.compose.foundation.background
 import androidx.compose.foundation.clickable
 import androidx.compose.foundation.layout.Arrangement
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.Row
 import androidx.compose.foundation.layout.WindowInsets
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.layout.size
 import androidx.compose.foundation.layout.systemBars
 import androidx.compose.foundation.layout.width
 import androidx.compose.foundation.layout.windowInsetsPadding
 import androidx.compose.foundation.lazy.LazyColumn
 import androidx.compose.foundation.lazy.items
 import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.filled.LocationOff
 import androidx.compose.material.icons.filled.LocationOn
 import androidx.compose.material.icons.rounded.Search
 import androidx.compose.material.icons.twotone.Delete
 import androidx.compose.material.icons.twotone.Flag
 import androidx.compose.material.icons.twotone.Refresh
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.Icon
 import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.ModalBottomSheet
 import androidx.compose.material3.Scaffold
 import androidx.compose.material3.Text
 import androidx.compose.material3.rememberModalBottomSheetState
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.collectAsState
 import androidx.compose.runtime.getValue
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.graphics.vector.ImageVector
 import androidx.compose.ui.text.font.FontWeight
 import androidx.compose.ui.tooling.preview.Preview
 import androidx.compose.ui.unit.dp
 import androidx.compose.ui.unit.sp
 import androidx.hilt.navigation.compose.hiltViewModel
 import androidx.navigation.NavController
 import com.example.weather.models.ui.search.SavedItemUiState
 import com.example.weather.models.ui.search.SearchItemUiState
 import com.example.weather.models.ui.search.SearchListItem
 import com.example.weather.models.ui.search.SearchListUiState
 import com.example.weather.models.ui.search.searchbar.SearchBarEvents
 import com.example.weather.screens.main.WeatherModalScreen
 import com.example.weather.widgets.SwipeRevealCard
 import com.example.weather.widgets.SwipeRevealCardButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    val searchBarUiState by viewModel.searchBarUiState.collectAsState()
    val searchResultListUiState by viewModel.searchListUiState.collectAsState()
    val modalSheetUiState by viewModel.modalSheetUiState.collectAsState()

    Scaffold(
        topBar = {
            SearchTopFieldBar(
                uiState = searchBarUiState,
                events = SearchBarEvents(
                    searchTextChange = { viewModel.setSearchBarText(it) },
                    onSearch = { viewModel.onSearch(it) },
                    isLoading = { viewModel.setSearchBarLoadingState(it) },
                    onExitRoute = { navController.popBackStack() }
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        MainLayout(
            modifier = Modifier.padding(innerPadding),
            searchListUiState = searchResultListUiState,
            searchText = searchBarUiState.searchText,
            onLocationSelected = { locationSelected, isLocationSaved ->
                viewModel.onLocationSelected(
                    location = locationSelected,
                    isSaved = isLocationSaved
                )

                /*navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(AppNavKeys.LOCATION_NAME, locationClicked)

                navController.popBackStack()*/
            },
        ) {
            if (modalSheetUiState.showSheet) {
                val sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )

                ModalBottomSheet(
                    onDismissRequest = { viewModel.onDismissBottomSheet() },
                    sheetState = sheetState,
                    shape = MaterialTheme.shapes.small,
                    dragHandle = {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .width(32.dp)
                                .height(4.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    shape = MaterialTheme.shapes.extraLarge
                                )
                        )
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .windowInsetsPadding(WindowInsets.systemBars)
                ) {
                    if (modalSheetUiState.locationSelected.isNotBlank()) {
                        WeatherModalScreen(
                            locationName = modalSheetUiState.locationSelected,
                            locationSaved = modalSheetUiState.isLocationSaved,
                        ) { onSave ->
                            viewModel.onLocationSaveStatusChanged(onSave)
                        }
                    } else {
                        Text("There was an issue loading this location.")
                    }
                }
            }
        }
    }
}

@Composable
private fun MainLayout(
    modifier: Modifier = Modifier,
    searchListUiState: SearchListUiState,
    searchText: String,
    onLocationSelected: (String, Boolean) -> Unit,
    modalBottomSheet: @Composable () -> Unit
) {
    val searchItems: List<SearchListItem> = searchListUiState.items.toList()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {

        modalBottomSheet()

        if (searchItems.isNotEmpty()) {
            LazyColumn (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            ) {
                items(searchItems) { item ->
                    if (searchText.isEmpty() && item is SavedItemUiState) {
                        SavedItem(savedItem = item)
                    } else if (searchText.isNotEmpty() && item is SearchItemUiState) {
                        SearchedItem(searchItem = item) { locationClicked, isLocationSaved ->
                            onLocationSelected(locationClicked, isLocationSaved)
                        }
                    }
                }
            }
        } else if (searchText.isNotEmpty() && searchItems.isEmpty()) {
            NoLocationFoundView(
                mainMsg = "No location found",
                subMsg = "Try searching for another location",
                image = Icons.Default.LocationOff
            )
        } else {
            NoLocationFoundView(
                mainMsg = "No location saved",
                subMsg = "Try searching for a location",
                image = Icons.Default.LocationOn
            )
        }
    }
}

@Composable
private fun NoLocationFoundView(
    mainMsg: String,
    subMsg: String,
    image: ImageVector
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = image,
            contentDescription = "Location Image",
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = mainMsg,
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp
        )
        Text(
            text = subMsg,
            fontSize = 22.sp
        )
    }
}

@Composable
private fun SavedItem(savedItem: SavedItemUiState) {
    SwipeRevealCard(
        frontContent = {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = savedItem.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${savedItem.currentTemp}°",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                ) {
                    Text(
                        text = savedItem.descriptionTemp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "L: ${savedItem.lowAndHighTemp.first}° | H: ${savedItem.lowAndHighTemp.second}°",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        hiddenContent = { hideContainer ->
            SwipeRevealCardButton(
                iconImage = Icons.TwoTone.Flag,
                iconDescription = "Flag Location",
                backgroundColor = Color(0xF7FFB300)
            ) {
                println("Rez Click 1")
                hideContainer()
            }

            SwipeRevealCardButton(
                iconImage = Icons.TwoTone.Refresh,
                iconDescription = "Flag Location",
                backgroundColor = Color(0xD86B3A86)
            ) {
                println("Rez Click 2")
                hideContainer()
            }

            SwipeRevealCardButton(
                iconImage = Icons.TwoTone.Delete,
                iconDescription = "Flag Location",
                backgroundColor = MaterialTheme.colorScheme.error
            ) {
                println("Rez Click 3")
                hideContainer()
            }
        }
    )
}

@Composable
private fun SearchedItem(searchItem: SearchItemUiState, onLocationClick: (String, Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = {
                onLocationClick(
                    searchItem.name,
                    searchItem.saveTag
                )
            })
    ) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = "Search Icon",
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = searchItem.name ,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainLayoutWithSavedItemsPreview() {
    val mockSearchListUiState = SearchListUiState(
        items = setOf(SavedItemUiState())
    )

    MainLayout(
        searchListUiState = mockSearchListUiState,
        searchText = "",
        onLocationSelected = { _, _ -> },
        modalBottomSheet = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun MainLayoutWithSearchItemsPreview() {
    val mockSearchListUiState = SearchListUiState(
        items = setOf(SearchItemUiState())
    )

    MainLayout(
        searchListUiState = mockSearchListUiState,
        searchText = "Not Empty",
        onLocationSelected = { _, _ -> },
        modalBottomSheet = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun MainLayoutWithNoSaveViewPreview() {
    MainLayout(
        searchListUiState = SearchListUiState(),
        searchText = "",
        onLocationSelected = { _, _ -> },
        modalBottomSheet = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun MainLayoutWithNoResultViewPreview() {
    MainLayout(
        searchListUiState = SearchListUiState(),
        searchText = "Location",
        onLocationSelected = { _, _ -> },
        modalBottomSheet = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchedItemPreview() {
    SearchedItem(SearchItemUiState()) { _, _ ->

    }
}

@Preview(showBackground = true)
@Composable
private fun SavedItemPreview() {
    SavedItem(
        savedItem = SavedItemUiState()
    )
}