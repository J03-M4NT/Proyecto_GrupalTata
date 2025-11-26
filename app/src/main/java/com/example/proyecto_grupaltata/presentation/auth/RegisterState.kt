package com.example.proyecto_grupaltata.presentation.auth

/**
 * Clase de datos para manejar el estado del proceso de registro.
 */
data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
