package com.example.proyecto_grupaltata.presentation.auth

/**
 * Clase de datos para manejar el estado del proceso de inicio de sesión.
 */
data class LoginState(
    val isLoading: Boolean = false,
    // Guardamos el ID del usuario en caso de éxito para poder usarlo después
    val loginSuccess: String? = null,
    val error: String? = null
)
