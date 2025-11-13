package com.example.weather.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather.screens.WeatherSplashScreen
import com.example.weather.screens.main.WeatherScreen
import com.example.weather.screens.search.WeatherSearchScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.MainScreen.name,
    ) {
        //splashScreen(navController = navController)
        mainScreen(navController = navController)
        searchScreen(navController = navController)
    }
}

//!! Might Change
//Currently Not Used - Went with Android Default Icon Splash Screen
private fun NavGraphBuilder.splashScreen(navController: NavController) {
    composable(route = AppRoutes.SplashScreen.name) {
        WeatherSplashScreen(navController = navController)
    }
}

private fun NavGraphBuilder.mainScreen(navController: NavController) {
    composable(route = AppRoutes.MainScreen.name) { navBackStackEntry ->
        WeatherScreen(navController = navController)
    }
}

private fun NavGraphBuilder.searchScreen(navController: NavController) {
    val transitionTimeDuration = 500

    composable(
        route = AppRoutes.SearchScreen.name,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(transitionTimeDuration)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(transitionTimeDuration)

            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(transitionTimeDuration)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(transitionTimeDuration)

            )
        }
    ) {
        WeatherSearchScreen(navController = navController)
    }
}