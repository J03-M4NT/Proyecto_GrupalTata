package com.example.proyecto_grupaltata.presentation.vacancies

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyecto_grupaltata.domain.model.Vacancy

class VacanciesViewModel : ViewModel() {

    private val _vacancies = mutableStateListOf<Vacancy>()
    val vacancies: List<Vacancy> = _vacancies

    var searchQuery by mutableStateOf("")
        private set

    val filteredVacancies by derivedStateOf {
        if (searchQuery.isBlank()) {
            _vacancies
        } else {
            _vacancies.filter {
                it.profileName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun addVacancy(newVacancy: Vacancy) {
        _vacancies.add(newVacancy)
    }
}