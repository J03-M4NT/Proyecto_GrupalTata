package com.example.proyecto_grupaltata.domain.model

data class Candidate(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val role: String,
    val overallMatch: Int,
    val skills: List<SkillMatch>
)

data class SkillMatch(
    val name: String,
    val percentage: Int
)
