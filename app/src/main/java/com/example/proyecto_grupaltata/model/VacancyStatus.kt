package com.example.proyecto_grupaltata.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

// Nuevo modelo de datos específico para el Dashboard.
// No entra en conflicto con el modelo Vacancy existente.
data class VacancyStatus(
    val title: String = "",
    val isCovered: Boolean = false,
    @ServerTimestamp
    val createdAt: Date = Date()
)
