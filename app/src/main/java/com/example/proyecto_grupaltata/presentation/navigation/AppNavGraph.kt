package com.example.proyecto_grupaltata.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_grupaltata.presentation.auth.LoginScreen
import com.example.proyecto_grupaltata.presentation.matching.MatchingScreen
import com.example.proyecto_grupaltata.presentation.vacancies.VacanciesScreen


@Composable
fun AppNavGraph(){

    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = "login"){    // La primera ruta donde comienza la app

        composable("login") { LoginScreen(navController)}

        composable("vacancies") { VacanciesScreen(navController) } // Pass NavController

        composable(
            route = "matching/{skills}",
            arguments = listOf(navArgument("skills") { type = NavType.StringType })
        ) { backStackEntry ->
            val skillsString = backStackEntry.arguments?.getString("skills") ?: ""
            val skillsList = skillsString.split(",").filter { it.isNotEmpty() }
            MatchingScreen(navController = navController, skills = skillsList)
        }

    }
}