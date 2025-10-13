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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchTopFieldBar(
    searchText: String = "",
    searchTextState: (String) -> Unit,
    onSearch: (String) -> Unit,
    onExitRoute: () -> Unit = {},
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
                    value = searchText,
                    valueState = searchTextState
                ) {
                    onSearch(it)
                }
            }
        },
        colors =  TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
        navigationIcon = {
            IconButton(
                onClick = onExitRoute,
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
    value: String,
    valueState: (String) -> Unit,
    onSearch: (String) -> Unit,
) {
    //TODO: Use FocusRequester to to display keyboard immediately
    //Debouncer
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            valueState(newText)

            debounceJob?.cancel()
            if (newText.isNotBlank()) {
                debounceJob = coroutineScope.launch {
                    delay(2000L)
                    println("Rez UI $value | $newText")
                    onSearch(newText)
                }
            } else {
                onSearch("")
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
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        valueState("")
                        onSearch("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Clear Text Button"
                    )
                }
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
    SearchTopFieldBar(
        searchText = "Not Empty",
        searchTextState = {},
        onExitRoute = {},
        onSearch = {}
    )
}

@Preview
@Composable
private fun TopAppBarPreview() {
    SearchTopFieldBar(
        searchText = "",
        searchTextState = {},
        onExitRoute = {},
        onSearch = {},
    )
}
