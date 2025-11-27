package com.example.proyecto_grupaltata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_grupaltata.presentation.components.MainTopAppBar
import com.example.proyecto_grupaltata.presentation.main.MainViewModel
import com.example.proyecto_grupaltata.presentation.navigation.AppNavGraph
import com.example.proyecto_grupaltata.presentation.navigation.AppScreens
import com.example.proyecto_grupaltata.presentation.navigation.BottomNavigationBar
import com.example.proyecto_grupaltata.ui.theme.Proyecto_GrupalTataTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_GrupalTataTheme {
                MainScreen(mainViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(mainViewModel: MainViewModel) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val usuario by mainViewModel.usuario.collectAsState()

    Scaffold(
        topBar = {
            if (currentRoute != AppScreens.LoginScreen.route && currentRoute != AppScreens.RegisterScreen.route) {
                MainTopAppBar(usuario = usuario)
            }
        },
        bottomBar = {
            if (currentRoute != AppScreens.LoginScreen.route && currentRoute != AppScreens.RegisterScreen.route) {
                BottomNavigationBar(navController = navController, usuario = usuario)
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            mainViewModel = mainViewModel
        )
    }
}
