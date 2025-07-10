package com.example.weather.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather.screens.main.WeatherMainScreen
import com.example.weather.screens.WeatherSplashScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.name,
    ) {
        composable(route = AppScreens.SplashScreen.name) {
            WeatherSplashScreen(navController = navController)
        }

        composable(route = AppScreens.MainScreen.name) {
            WeatherMainScreen(navController = navController)
        }
    }
}