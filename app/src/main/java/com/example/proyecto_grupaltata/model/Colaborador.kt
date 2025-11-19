package com.example.proyecto_grupaltata.model

// Data class para representar una habilidad, ya sea técnica o blanda
data class Skill(
    val name: String = "",
    val percentage: Int = 0
)

// Data class principal que coincide con la estructura del Firestore
data class Colaborador(
    var id: String? = null,
    val nombre: String = "",
    val puesto: String = "", // Coincide con Firestore
    val softSkills: List<Skill> = emptyList(), // Coincide con Firestore
    val technicalSkills: List<Skill> = emptyList() // Coincide con Firestore
)
