package com.example.proyecto_grupaltata.domain.model

// Add the no-arg constructor and default values for Firestore deserialization
data class Vacancy(
    var profileName: String = "",
    var projectAccount: String = "",
    var requiredSkills: List<String> = emptyList(),
    var desiredLevel: String = "",
    var startDate: String = "",
    var urgency: String = "",
    var id: String = ""
)