package com.example.proyecto_grupaltata.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_grupaltata.presentation.auth.LoginScreen
import com.example.proyecto_grupaltata.presentation.vacancies.VacanciesScreen


@Composable
fun AppNavGraph(){

    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = "login"){    // La primera ruta donde comienza la app

        composable("login") { LoginScreen(navController)}

        // The register_vacancy route is no longer a full screen, but a dialog.
        // It will be called from VacanciesScreen.

        composable("vacancies") { VacanciesScreen() }

    }
}