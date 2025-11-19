// Forzando la reescritura del archivo
package com.example.proyecto_grupaltata.presentation.mapping

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_grupaltata.model.Colaborador
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Estado de la UI para manejar Carga, Éxito y Error
sealed interface MappingUiState {
    object Loading : MappingUiState
    object Success : MappingUiState
    data class Error(val message: String) : MappingUiState
}

class MappingViewModel : ViewModel() {

    // --- Estados del ViewModel ---
    private val _uiState = mutableStateOf<MappingUiState>(MappingUiState.Loading)
    val uiState: State<MappingUiState> = _uiState

    // Flujo para el texto de búsqueda
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    // Flujo para la lista original de colaboradores desde Firebase
    private val _collaborators = MutableStateFlow<List<Colaborador>>(emptyList())

    // Flujo combinado que filtra los colaboradores según el texto de búsqueda
    val filteredCollaborators = searchText
        .combine(_collaborators) { text, collaborators ->
            if (text.isBlank()) {
                collaborators
            } else {
                collaborators.filter {
                    it.nombre.contains(text, ignoreCase = true)
                }
            }
        }

    init {
        fetchCollaborators()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    // --- FUNCIÓN CORREGIDA Y ROBUSTA ---
    private fun fetchCollaborators() {
        viewModelScope.launch {
            _uiState.value = MappingUiState.Loading
            try {
                val snapshot = Firebase.firestore.collection("collaborators").get().await()
                val collaboratorsList = mutableListOf<Colaborador>()

                // Recorremos los documentos uno por uno para evitar crashes por datos inconsistentes
                for (document in snapshot.documents) {
                    try {
                        // Intentamos convertir el documento a un objeto Colaborador
                        val collaborator = document.toObject<Colaborador>()
                        if (collaborator != null) {
                            // Si la conversión es exitosa, le asignamos el ID del documento y lo añadimos a la lista
                            collaboratorsList.add(collaborator.copy(id = document.id))
                        }
                    } catch (e: Exception) {
                        // Si un documento específico falla, lo ignoramos y registramos el error
                        Log.e("MappingViewModel", "Error al convertir el documento ${document.id}", e)
                    }
                }

                _collaborators.value = collaboratorsList
                _uiState.value = MappingUiState.Success

            } catch (e: Exception) {
                _uiState.value = MappingUiState.Error("Error al cargar los datos: ${e.message}")
            }
        }
    }
}
