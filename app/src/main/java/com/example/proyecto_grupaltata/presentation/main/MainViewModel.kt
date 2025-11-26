package com.example.proyecto_grupaltata.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_grupaltata.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel : ViewModel() {

    // **RESTAURACIÓN: Usar el objeto Usuario, no Colaborador**
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario = _usuario.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun fetchCollaborator(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // **RESTAURACIÓN: Leer de la colección 'usuarios' para obtener el rol**
                val document = FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(userId)
                    .get()
                    .await()

                _usuario.value = if (document.exists()) {
                    document.toObject(Usuario::class.java)
                } else {
                    Log.w("MainViewModel", "No se encontró un perfil para el userId: $userId")
                    null
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error al obtener datos del usuario", e)
                _usuario.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearCollaborator() {
        _usuario.value = null
    }
}
