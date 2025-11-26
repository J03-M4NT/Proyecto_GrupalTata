package com.example.proyecto_grupaltata.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyecto_grupaltata.presentation.auth.LoginScreen
import com.example.proyecto_grupaltata.presentation.auth.RegisterScreen
import com.example.proyecto_grupaltata.presentation.brechas.BrechasScreen
import com.example.proyecto_grupaltata.presentation.home.HomeScreen
import com.example.proyecto_grupaltata.presentation.main.MainViewModel
import com.example.proyecto_grupaltata.presentation.matching.MatchingScreen
import com.example.proyecto_grupaltata.presentation.more.MoreScreen
import com.example.proyecto_grupaltata.presentation.profile.EditProfileScreen
import com.example.proyecto_grupaltata.presentation.profile.ProfileScreen
import com.example.proyecto_grupaltata.presentation.report_activity.ReportActivityScreen
import com.example.proyecto_grupaltata.presentation.vacancies.VacanciesScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier, mainViewModel: MainViewModel) {

    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route,
        modifier = modifier
    ) {

        composable(AppScreens.LoginScreen.route) { LoginScreen(navController, mainViewModel) }
        composable(AppScreens.RegisterScreen.route) { RegisterScreen(navController) }
        composable(AppScreens.HomeScreen.route) { HomeScreen(navController) }
        composable(AppScreens.VacanciesScreen.route) { VacanciesScreen(navController) }
        composable(AppScreens.MatchingScreen.route) { MatchingScreen(navController) }
        composable(AppScreens.BrechasScreen.route) { BrechasScreen() }
        composable(AppScreens.ReportActivityScreen.route) { ReportActivityScreen(navController) }
        composable(AppScreens.ProfileScreen.route) { ProfileScreen(navController) }
        
        // **RESTAURACIÓN: Pasar el MainViewModel a MoreScreen**
        composable(AppScreens.MoreScreen.route) { MoreScreen(navController, mainViewModel) }

        composable(
            route = AppScreens.EditProfileScreen.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            requireNotNull(userId) { "El ID de usuario no puede ser nulo." }
            EditProfileScreen(navController = navController, userId = userId)
        }
    }
}