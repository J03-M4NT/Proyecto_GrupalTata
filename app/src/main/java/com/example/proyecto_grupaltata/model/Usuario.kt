package com.example.proyecto_grupaltata.model

import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Modelo de datos para la colección `usuarios`.
 * Contiene la información esencial para la autenticación y los roles.
 */
@IgnoreExtraProperties
data class Usuario(
    val nombre: String = "",
    val correo: String = "",
    val rol: String = ""
) {
    // Constructor vacío requerido por Firestore
    constructor() : this("", "", "")
}
