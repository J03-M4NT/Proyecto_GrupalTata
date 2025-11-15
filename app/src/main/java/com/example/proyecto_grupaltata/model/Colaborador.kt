package com.example.proyecto_grupaltata.model

data class Colaborador(
    var id: String? = null,
    var nombre: String = "",
    var rol: String = "",
    var skills: List<String> = emptyList(),
    var nivel: String = "",
    var certificaciones: String = "",
    var movilidad: Boolean = false,
    var fechaActualizacion: Long = System.currentTimeMillis()
)
