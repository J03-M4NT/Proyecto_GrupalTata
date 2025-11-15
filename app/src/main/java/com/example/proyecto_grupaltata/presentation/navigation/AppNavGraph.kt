package com.example.proyecto_grupaltata.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_grupaltata.presentation.auth.LoginScreen
// IMPORTS AÑADIDOS
import com.example.proyecto_grupaltata.presentation.colaborador.AddColaboradorScreen
import com.example.proyecto_grupaltata.presentation.home.HomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route
    ) {
        // Ruta para la pantalla de Login
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }

        // Ruta para la pantalla Principal (Home) - ¡AHORA SÍ LA ENCUENTRA!
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController)
        }

        // Ruta para añadir colaborador - ¡AHORA SÍ LA ENCUENTRA!
        composable(route = AppScreens.AddColaboradorScreen.route) {
            AddColaboradorScreen(navController)
        }
    }
}
