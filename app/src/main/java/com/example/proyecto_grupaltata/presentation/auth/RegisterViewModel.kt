package com.example.proyecto_grupaltata.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_grupaltata.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel : ViewModel() {

    // Propiedades para los campos del formulario
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // Estado de la operación de registro
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    // Instancias de Firebase
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Actualiza los valores de los campos del formulario.
     */
    fun onValueChange(value: String, field: String) {
        when (field) {
            "username" -> username = value
            "email" -> email = value
            "password" -> password = value
        }
    }

    /**
     * Orquesta el proceso de registro de un nuevo usuario.
     */
    fun registerUser() {
        // Validaciones básicas
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = RegisterState(error = "Todos los campos son obligatorios")
            return
        }
        if (password.length < 6) {
            _registerState.value = RegisterState(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _registerState.value = RegisterState(isLoading = true)
            try {
                // 1. Crear el usuario en Firebase Authentication
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user
                requireNotNull(firebaseUser) { "Error al crear el usuario en Firebase Auth." }

                // 2. Crear el objeto Usuario con el rol por defecto
                val newUser = Usuario(
                    nombre = username,
                    correo = email,
                    rol = "Colaborador" // Rol por defecto
                )

                // 3. Guardar el objeto en la colección "usuarios" de Firestore
                db.collection("usuarios").document(firebaseUser.uid).set(newUser).await()

                // 4. Notificar que el registro fue exitoso
                _registerState.value = RegisterState(isSuccess = true)

            } catch (e: Exception) {
                // Manejar errores (ej: correo ya en uso)
                val errorMessage = e.message ?: "Ocurrió un error inesperado"
                _registerState.value = RegisterState(error = errorMessage)
            } finally {
                // Asegurarse de que el estado de carga se desactive
                _registerState.value = _registerState.value.copy(isLoading = false)
            }
        }
    }
}
