package com.example.weather.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.models.ui.search.searchbar.SearchBarEvents
import com.example.weather.models.ui.search.searchbar.SearchBarUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchTopFieldBar(
    uiState: SearchBarUiState,
    events: SearchBarEvents,
    customActions: @Composable () -> Unit = {},
) {

    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                SearchField(
                    uiState = uiState,
                    events = events
                )
            }
        },
        colors =  TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
        navigationIcon = {
            IconButton(
                onClick = events.onExitRoute,
                modifier = Modifier.size(24.dp)
                ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back icon",
                )
            }
        },
        actions = { customActions() }
    )
}

@Composable
private fun SearchField(
    uiState: SearchBarUiState,
    events: SearchBarEvents,
) {
    //TODO: Use FocusRequester to to display keyboard immediately
    //Debouncer
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    OutlinedTextField(
        value = uiState.searchText,
        onValueChange = { newText ->
            events.searchTextChange(newText)

            debounceJob?.cancel()
            if (newText.isNotBlank()) {
                events.isLoading(true)
                debounceJob = coroutineScope.launch {
                    delay(1000L)
                    events.onSearch(newText)
                }
            } else {
                events.onSearch("")
                events.isLoading(false)
            }
        },
        textStyle =  TextStyle(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        placeholder = {
            Text("Search for a location")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Clear Text Button"
            )
        },
        trailingIcon = {
            if (uiState.searchText.isNotBlank() && !uiState.isLoading) {
                IconButton(
                    onClick = {
                        events.searchTextChange("")
                        events.onSearch("")
                        events.isLoading(false)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Clear Text Button"
                    )
                }
            } else if (uiState.isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        singleLine = true,
        shape  = RoundedCornerShape(26.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
}

@Preview
@Composable
private fun TopAppBarMainPreview() {
    val uiState = SearchBarUiState(searchText = "Not Empty")
    val events = SearchBarEvents({}, {}, {})
    SearchTopFieldBar(
        uiState = uiState,
        events = events
    )
}

@Preview
@Composable
private fun TopAppBarPreview() {
    val uiState = SearchBarUiState(searchText = "")
    val events = SearchBarEvents({}, {}, {})
    SearchTopFieldBar(
        uiState = uiState,
        events = events
    )
}
