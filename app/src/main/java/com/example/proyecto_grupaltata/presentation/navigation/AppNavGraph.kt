package com.example.proyecto_grupaltata.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_grupaltata.presentation.auth.LoginScreen


@Composable
fun AppNavGraph(){

    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = "login"){    // La primera ruta donde comienza la app

        // Es necesario?
        //composable("register") { RegisterScreen(navController) }
        composable("login") { LoginScreen(navController)}

        // DrawerScaffold, posiblemente se añada, pero deberia haber una forma de ponerlo para que este debajo
        // en todo caso seria hacer un barra de navegacion




    }

    // ----------------------------------------------------------

}