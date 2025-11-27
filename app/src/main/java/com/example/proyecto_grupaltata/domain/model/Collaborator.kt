package com.example.proyecto_grupaltata.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Modelo de datos que representa a un colaborador.
 * La anotación @IgnoreExtraProperties es crucial para evitar que la app se bloquee si
 * Firestore tiene campos que no están definidos en esta clase.
 */
@IgnoreExtraProperties
data class Collaborator(
    // El ID del documento no es parte de los datos, se asignará manualmente.
    var userId: String = "",

    // Campos que coinciden con la base de datos de Firestore
    val nombre: String = "",
    val rol: String = "",
    val nivel: String = "",
    val project: String = "",
    val skills: List<String> = emptyList(),
    val certificaciones: List<String> = emptyList(),
    val movilidad: Boolean = false
)
