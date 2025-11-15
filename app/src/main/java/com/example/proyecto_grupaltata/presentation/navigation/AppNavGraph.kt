package com.example.proyecto_grupaltata.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyecto_grupaltata.presentation.auth.LoginScreen
import com.example.proyecto_grupaltata.presentation.home.HomeScreen
import com.example.proyecto_grupaltata.presentation.vacancies.VacanciesScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route,
        modifier = modifier
    ) {
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = AppScreens.VacanciesScreen.route) {
            VacanciesScreen(navController)
        }
        // Aquí irán las demás pantallas que agregues
    }
}