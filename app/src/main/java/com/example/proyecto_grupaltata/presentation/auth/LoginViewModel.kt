package com.example.proyecto_grupaltata.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val auth = FirebaseAuth.getInstance()

    fun onValueChange(value: String, field: String) {
        when (field) {
            "email" -> email = value
            "password" -> password = value
        }
    }

    fun loginUser() {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState(error = "Correo y contraseña no pueden estar vacíos")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid
                requireNotNull(userId) { "Error al obtener el ID del usuario." }
                _loginState.value = LoginState(loginSuccess = userId)
            } catch (e: Exception) {
                _loginState.value = LoginState(error = e.message ?: "Ocurrió un error inesperado")
            }
        }
    }

    /**
     * **NUEVO: Envía un correo para restablecer la contraseña.**
     */
    fun resetPassword() {
        if (email.isBlank()) {
            _loginState.value = LoginState(error = "Por favor, introduce tu correo para restablecer la contraseña")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            try {
                auth.sendPasswordResetEmail(email).await()
                _loginState.value = LoginState(toastMessage = "Se ha enviado un correo para restablecer tu contraseña")
            } catch (e: Exception) {
                _loginState.value = LoginState(error = e.message ?: "Error al enviar el correo")
            }
        }
    }

    /**
     * **NUEVO: Limpia el mensaje de toast después de mostrarlo.**
     */
    fun clearToastMessage() {
        _loginState.update { it.copy(toastMessage = null, isLoading = false) }
    }

    /**
     * **NUEVO: Limpia el error después de mostrarlo.**
     */
    fun clearError() {
        _loginState.update { it.copy(error = null, isLoading = false) }
    }
}
