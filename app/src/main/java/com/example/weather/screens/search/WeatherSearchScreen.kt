package com.example.weather.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.weather.widgets.TopBar

@Composable
fun WeatherSearchScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(onExitRoute = { navController.popBackStack() }) }
    ) { innerPadding ->
        SearchLayout(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
private fun SearchLayout(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text("Search Screen")
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchLayoutPreview() {
    SearchLayout()
}