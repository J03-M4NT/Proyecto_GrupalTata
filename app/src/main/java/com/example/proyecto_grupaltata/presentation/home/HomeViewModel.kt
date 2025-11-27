package com.example.proyecto_grupaltata.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_grupaltata.model.ActivityItem
import com.example.proyecto_grupaltata.model.DashboardStats
import com.example.proyecto_grupaltata.model.VacancyStatus // <-- CORREGIDO: Usamos el nuevo modelo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _dashboardStats = MutableStateFlow(DashboardStats())
    val dashboardStats = _dashboardStats.asStateFlow()

    private val _recentActivity = MutableStateFlow<List<ActivityItem>>(emptyList())
    val recentActivity = _recentActivity.asStateFlow()

    init {
        listenToDataChanges()
    }

    private fun listenToDataChanges() {
        viewModelScope.launch {
            // Listener para la colección de colaboradores
            db.collection("collaborators").addSnapshotListener { snapshot, e ->
                if (e != null) { return@addSnapshotListener }
                val collaboratorCount = snapshot?.size() ?: 0
                _dashboardStats.value = _dashboardStats.value.copy(collaboratorCount = collaboratorCount)
                generateRecentActivity()
            }

            // Listener para la colección de vacantes - AHORA USA VacancyStatus
            db.collection("vacancies").addSnapshotListener { snapshot, e ->
                if (e != null) { return@addSnapshotListener }
                // Convierte los documentos al nuevo modelo, ignorando los campos que no coinciden
                val vacancies = snapshot?.toObjects(VacancyStatus::class.java) ?: emptyList()
                val openVacancies = vacancies.count { !it.isCovered }
                _dashboardStats.value = _dashboardStats.value.copy(openVacanciesCount = openVacancies)
                generateRecentActivity()
            }
        }
    }

    private fun generateRecentActivity() {
        viewModelScope.launch {
            val vacancyQuery = db.collection("vacancies")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(3)

            vacancyQuery.get().addOnSuccessListener { snapshot ->
                // Convierte también aquí a VacancyStatus
                val activities = snapshot.toObjects(VacancyStatus::class.java).map { vacancy ->
                    ActivityItem(
                        title = if (vacancy.isCovered) "Vacante cubierta" else "Nueva vacante registrada",
                        subtitle = vacancy.title,
                        // Firestore Timestamp se convierte a Date, lo necesitamos de vuelta a Timestamp para ActivityItem
                        timestamp = com.google.firebase.Timestamp(vacancy.createdAt ?: Date()),
                        type = if (vacancy.isCovered) "VACANCY_FILLED" else "NEW_VACANCY"
                    )
                }
                _recentActivity.value = activities.sortedByDescending { it.timestamp }
            }
        }
    }
}