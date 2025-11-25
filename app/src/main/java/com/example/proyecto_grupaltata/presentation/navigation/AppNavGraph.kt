package com.example.proyecto_grupaltata.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyecto_grupaltata.presentation.auth.LoginScreen
import com.example.proyecto_grupaltata.presentation.brechas.BrechasScreen
import com.example.proyecto_grupaltata.presentation.home.HomeScreen
import com.example.proyecto_grupaltata.presentation.mapping.MappingViewModel
import com.example.proyecto_grupaltata.presentation.matching.MatchingScreen
import com.example.proyecto_grupaltata.presentation.more.MoreScreen
import com.example.proyecto_grupaltata.presentation.report_activity.ReportActivityScreen
import com.example.proyecto_grupaltata.presentation.vacancies.VacanciesScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier) {

    // Create a single instance of the ViewModel to be shared
    val mappingViewModel: MappingViewModel = viewModel()

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = AppScreens.LoginScreen.route
    ) {

        composable(AppScreens.LoginScreen.route) { LoginScreen(navController) }

        composable(AppScreens.HomeScreen.route) { HomeScreen(navController) }

        composable(AppScreens.VacanciesScreen.route) {
            // Pass the shared ViewModel to VacanciesScreen
            VacanciesScreen(navController = navController, mappingViewModel = mappingViewModel)
        }

        composable(AppScreens.MoreScreen.route) { MoreScreen(navController) }

        composable(AppScreens.MatchingScreen.route) {
            // Pass the shared ViewModel to MatchingScreen (Mapping)
            MatchingScreen(navController = navController, mappingViewModel = mappingViewModel)
        }

        composable(AppScreens.BrechasScreen.route) { BrechasScreen() }

        composable(AppScreens.ReportActivityScreen.route) { ReportActivityScreen(navController) }

    }
}