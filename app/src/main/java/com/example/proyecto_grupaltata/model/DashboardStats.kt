package com.example.proyecto_grupaltata.model

// Data class para las estadísticas del dashboard
data class DashboardStats(
    val collaboratorCount: Int = 0,
    val openVacanciesCount: Int = 0,
    val mappedSkillsCount: Int = 0, // Por ahora estático, luego se puede calcular
    val internalMatchPercentage: Int = 0 // Por ahora estático, luego se puede calcular
)