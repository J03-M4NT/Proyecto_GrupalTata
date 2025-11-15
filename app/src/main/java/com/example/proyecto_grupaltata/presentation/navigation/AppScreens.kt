package com.example.proyecto_grupaltata.presentation.navigation

// Usamos un "sealed class" para definir todas las pantallas de nuestra app
sealed class AppScreens(val route: String) {


    // Pantalla de Login
    object LoginScreen : AppScreens("login_screen")


    // Pantalla Principal (Home)
    object HomeScreen : AppScreens("home_screen")


    // Pantalla para Añadir Colaborador
    object AddColaboradorScreen : AppScreens("add_colaborador_screen")


    // Pantalla de Vacantes
    object VacanciesScreen : AppScreens("vacancies_screen")

}
