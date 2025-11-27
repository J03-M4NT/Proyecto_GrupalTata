package com.example.proyecto_grupaltata.presentation.auth

/**
 * Clase de datos para manejar el estado del proceso de inicio de sesión.
 */
data class LoginState(
    val isLoading: Boolean = false,
    val loginSuccess: String? = null,
    val error: String? = null,
    // **NUEVO: Mensaje para mostrar en un Toast (ej. confirmación de correo enviado)**
    val toastMessage: String? = null 
)
