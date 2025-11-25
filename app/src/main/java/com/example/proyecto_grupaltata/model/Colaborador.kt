package com.example.proyecto_grupaltata.model

import com.google.firebase.firestore.PropertyName

// Data class para representar una habilidad, ya sea técnica o blanda
data class Skill(
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("percentage") @set:PropertyName("percentage") var percentage: Int = 0
)

// Data class principal que coincide con la estructura del Firestore
data class Colaborador(
    var id: String? = null,
    
    @get:PropertyName("nombre") @set:PropertyName("nombre") var nombre: String = "",
    
    // --- SOLUCIÓN DEFINITIVA ---
    // Esta anotación conecta el campo "Puesto" de Firebase con la variable "puesto" de Kotlin.
    @get:PropertyName("Puesto") @set:PropertyName("Puesto") var puesto: String = "", 
    
    @get:PropertyName("softSkills") @set:PropertyName("softSkills") var softSkills: List<Skill> = emptyList(),
    @get:PropertyName("technicalSkills") @set:PropertyName("technicalSkills") var technicalSkills: List<Skill> = emptyList()
)