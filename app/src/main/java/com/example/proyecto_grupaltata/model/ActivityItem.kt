package com.example.proyecto_grupaltata.model

import com.google.firebase.Timestamp

// Data class para un item en la lista de "Actividad Reciente"
data class ActivityItem(
    val title: String = "",
    val subtitle: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val type: String = "" // Ej: "NEW_VACANCY", "VACANCY_FILLED", "NEW_COLLABORATOR"
)