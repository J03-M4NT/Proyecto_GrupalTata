package com.example.proyecto_grupaltata.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    // Propiedades para los campos del formulario
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // Estado de la operación de login
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    // Instancia de Firebase
    private val auth = FirebaseAuth.getInstance()

    /**
     * Actualiza los valores de los campos del formulario.
     */
    fun onValueChange(value: String, field: String) {
        when (field) {
            "email" -> email = value
            "password" -> password = value
        }
    }

    /**
     * Orquesta el proceso de inicio de sesión.
     */
    fun loginUser() {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState(error = "Correo y contraseña no pueden estar vacíos")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            try {
                // 1. Iniciar sesión con Firebase Auth
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid
                requireNotNull(userId) { "Error al obtener el ID del usuario." }

                // 2. Notificar que el login fue exitoso, pasando el ID del usuario
                _loginState.value = LoginState(loginSuccess = userId)

            } catch (e: Exception) {
                val errorMessage = e.message ?: "Ocurrió un error inesperado"
                _loginState.value = LoginState(error = errorMessage)
            } finally {
                // No desactivamos el loading aquí para dar tiempo a la navegación
            }
        }
    }
}
