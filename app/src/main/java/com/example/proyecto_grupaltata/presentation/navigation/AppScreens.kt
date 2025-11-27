package com.example.proyecto_grupaltata.presentation.navigation

// Usamos un "sealed class" para definir todas las pantallas de nuestra app
sealed class AppScreens(val route: String) {


    // Pantalla de Login
    object LoginScreen : AppScreens("login_screen")

    // **ARREGLO: Pantalla de Registro que faltaba**
    object RegisterScreen : AppScreens("register_screen")

    // Pantalla Principal (Home)
    object HomeScreen : AppScreens("home_screen")


    // Pantalla para Añadir Colaborador
    object AddColaboradorScreen : AppScreens("add_colaborador_screen")


    // Pantalla de Vacantes
    object VacanciesScreen : AppScreens("vacancies_screen")

    // Pantalla de Matching (Skill Mapping)
    object MatchingScreen : AppScreens("matching_screen")

    // Pantalla "Más"
    object MoreScreen : AppScreens("more_screen")

    // Pantalla de Brechas
    object BrechasScreen : AppScreens("brechas_screen")
    
    // Pantalla de Reportes de Actividad
    object ReportActivityScreen : AppScreens("report_activity_screen")

    // Pantalla de Perfil de Usuario
    object ProfileScreen : AppScreens("profile_screen")

    // Pantalla para Editar el Perfil de Usuario. Acepta el ID de usuario como argumento.
    object EditProfileScreen : AppScreens("edit_profile_screen/{userId}") {
        // Función para construir la ruta completa con el ID de usuario.
        fun createRoute(userId: String) = "edit_profile_screen/$userId"
    }

}
