package com.example.proyecto_grupaltata.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto_grupaltata.model.Usuario

@Composable
fun BottomNavigationBar(navController: NavController, usuario: Usuario?) {

    // **RESTAURACIÓN: La pestaña "Más" es visible para TODOS los usuarios.**
    val allItems = listOf(
        BottomNavItem("Inicio", AppScreens.HomeScreen.route, Icons.Default.Home),
        BottomNavItem("Mapping", AppScreens.MatchingScreen.route, Icons.Default.Person),
        BottomNavItem("Vacantes", AppScreens.VacanciesScreen.route, Icons.Default.Work),
        BottomNavItem("Brechas", AppScreens.BrechasScreen.route, Icons.AutoMirrored.Filled.TrendingUp),
        BottomNavItem("Más", AppScreens.MoreScreen.route, Icons.Default.Menu) // La pestaña "Más" es para todos
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        allItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = { navigateTo(navController, item.route) }
            )
        }
    }
}

private fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { startRoute ->
            popUpTo(startRoute) { saveState = true }
        }
        launchSingleTop = true
        restoreState = true
    }
}

data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)
