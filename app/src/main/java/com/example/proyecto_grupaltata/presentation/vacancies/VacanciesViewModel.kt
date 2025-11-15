package com.example.proyecto_grupaltata.presentation.vacancies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_grupaltata.data.remote.firebase.FirestoreManager
import com.example.proyecto_grupaltata.domain.model.Vacancy
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VacanciesViewModel : ViewModel() {

    private val firestoreManager = FirestoreManager()

    // Expose the raw flow of vacancies from Firestore
    val vacancies: StateFlow<List<Vacancy>> = firestoreManager.getVacanciesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Expose the search query state
    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun addVacancy(newVacancy: Vacancy) {
        viewModelScope.launch {
            firestoreManager.addVacancy(newVacancy)
        }
    }
}