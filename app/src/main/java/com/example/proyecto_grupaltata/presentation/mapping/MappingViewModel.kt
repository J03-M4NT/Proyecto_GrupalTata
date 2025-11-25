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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed interface MappingUiState {
    object Loading : MappingUiState
    object Success : MappingUiState
    data class Error(val message: String) : MappingUiState
}

class MappingViewModel : ViewModel() {

    private val _uiState = mutableStateOf<MappingUiState>(MappingUiState.Loading)
    val uiState: State<MappingUiState> = _uiState

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _collaborators = MutableStateFlow<List<Colaborador>>(emptyList())
    
    // --- NUEVO: Estado para el diálogo ---
    private val _selectedCollaborator = MutableStateFlow<Colaborador?>(null)
    val selectedCollaborator: StateFlow<Colaborador?> = _selectedCollaborator.asStateFlow()

    val filteredCollaborators = searchText
        .combine(_collaborators) { text, collaborators ->
            if (text.isBlank()) {
                collaborators
            } else {
                collaborators.filter { it.nombre.contains(text, ignoreCase = true) }
            }
        }

    init {
        fetchCollaborators()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
    
    // --- NUEVAS FUNCIONES para manejar el diálogo ---
    fun onCollaboratorSelected(collaborator: Colaborador) {
        _selectedCollaborator.value = collaborator
    }

    fun onDialogDismiss() {
        _selectedCollaborator.value = null
    }

    private fun fetchCollaborators() {
        viewModelScope.launch {
            _uiState.value = MappingUiState.Loading
            try {
                val snapshot = Firebase.firestore.collection("collaborators").get().await()
                val collaboratorsList = mutableListOf<Colaborador>()

                for (document in snapshot.documents) {
                    try {
                        val collaborator = document.toObject<Colaborador>()
                        if (collaborator != null) {
                            collaboratorsList.add(collaborator.copy(id = document.id))
                        }
                    } catch (e: Exception) {
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