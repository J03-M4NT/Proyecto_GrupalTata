package com.example.proyecto_grupaltata.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_grupaltata.presentation.matching.MatchingScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyecto_grupaltata.presentation.auth.LoginScreen
import com.example.proyecto_grupaltata.presentation.home.HomeScreen
import com.example.proyecto_grupaltata.presentation.vacancies.VacanciesScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier) {


    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route){    // La primera ruta donde comienza la app

        composable(AppScreens.LoginScreen.route) { LoginScreen(navController) }
        
        composable(AppScreens.HomeScreen.route) { HomeScreen(navController) }
        
        composable(AppScreens.VacanciesScreen.route) { VacanciesScreen(navController) }
        
        composable(
            route = "matching/{skills}",
            arguments = listOf(navArgument("skills") { type = NavType.StringType })
        ) { backStackEntry ->
            val skillsString = backStackEntry.arguments?.getString("skills") ?: ""
            val skillsList = skillsString.split(",").filter { it.isNotEmpty() }
            MatchingScreen(navController = navController, skills = skillsList)
        }
 
        // Demas pantallas
        
    }
}