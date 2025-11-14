package com.example.proyecto_grupaltata.domain.model

data class Vacancy(
    val profileName: String,
    val projectAccount: String,
    val requiredSkills: List<String>,
    val desiredLevel: String,
    val startDate: String,
    val urgency: String,
    // We can add more fields later, like a unique ID
    val id: String = java.util.UUID.randomUUID().toString() 
)