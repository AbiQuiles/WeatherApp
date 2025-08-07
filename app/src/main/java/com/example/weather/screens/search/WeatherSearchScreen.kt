package com.example.weather.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherSearchScreen() {
    Scaffold (
        topBar = { TopSearchBar() },
        containerColor = MaterialTheme.colorScheme.surface
    ){ innerPadding ->
        SearchLayout(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
 fun SearchLayout(modifier: Modifier = Modifier) {
    LazyColumn (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 18.dp, horizontal = 6.dp)
    ) {
        items(15) {
            LocationItem()
            HorizontalDivider(
                thickness = 0.2.dp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopSearchBar() {
    var textState by remember {
        mutableStateOf("")
    }
    var expandedState by remember {
        mutableStateOf(false)
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = textState,
                    onQueryChange = { textState = it },
                    onSearch = { expandedState = false },
                    expanded = expandedState,
                    onExpandedChange = { expandedState = it },
                    placeholder = { Text("Search for a location") },
                    leadingIcon = {
                        if (expandedState) {
                            IconButton(
                                onClick = {
                                    if (expandedState) {
                                        expandedState = false
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back Button")
                            }
                        }
                    },
                    trailingIcon = {
                        if (expandedState && textState.isNotEmpty()) {
                            IconButton(
                                onClick = { textState = ""}
                            ) {
                                Icon(Icons.Default.Cancel, contentDescription = "Clear Text Button")
                            }
                        }
                    }
                )
            },
            expanded = expandedState,
            onExpandedChange = { expandedState = it },
        ) {
            Column(modifier = Modifier.padding(6.dp)
            ) {
                Text("Item")
                Text("Item")
                Text("Item")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationItem() {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Orlando",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "200°",
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
                text = "Sunny",
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "L: -200° |  H: 200°",
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TopSearchBarPreview() {
    TopSearchBar()
}

@Preview(showBackground = true)
@Composable
private fun SearchLayoutPreview() {
    SearchLayout()
}