package com.example.proyecto_grupaltata.presentation.matching

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_grupaltata.data.repository.MatchingRepository
import com.example.proyecto_grupaltata.domain.model.Candidate
import kotlinx.coroutines.launch

sealed class MatchingUiState {
    object Loading : MatchingUiState()
    data class Success(val candidates: List<Candidate>) : MatchingUiState()
    data class Error(val message: String) : MatchingUiState()
}

class MatchingViewModel : ViewModel() {

    private val repository = MatchingRepository()

    private val _uiState = mutableStateOf<MatchingUiState>(MatchingUiState.Loading)
    val uiState: State<MatchingUiState> = _uiState

    fun findCandidates(skills: List<String>) {
        viewModelScope.launch {
            _uiState.value = MatchingUiState.Loading
            try {
                val candidates = repository.findMatches(skills)
                _uiState.value = MatchingUiState.Success(candidates)
            } catch (e: Exception) {
                _uiState.value = MatchingUiState.Error("Error encontrando candidatos: ${e.message}")
            }
        }
    }
}