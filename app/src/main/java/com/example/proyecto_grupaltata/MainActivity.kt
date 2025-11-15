package com.example.proyecto_grupaltata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_grupaltata.presentation.navigation.AppNavGraph
import com.example.proyecto_grupaltata.presentation.navigation.AppScreens
import com.example.proyecto_grupaltata.presentation.navigation.BottomNavigationBar
import com.example.proyecto_grupaltata.ui.theme.Proyecto_GrupalTataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_GrupalTataTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Solo mostramos la barra si no estamos en la pantalla de Login
            if (currentRoute != AppScreens.LoginScreen.route) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }

}
