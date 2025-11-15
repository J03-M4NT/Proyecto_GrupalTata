package com.example.proyecto_grupaltata.data.repository

import com.example.proyecto_grupaltata.domain.model.Candidate
import com.example.proyecto_grupaltata.domain.model.SkillMatch
import kotlinx.coroutines.delay

class MatchingRepository {

    // This function simulates a network call to the matching API.
    // We'll replace this with a real call to Retrofit later.
    suspend fun findMatches(skills: List<String>): List<Candidate> {
        // Simulate network delay
        delay(1500) 

        // Return a hardcoded list of candidates for now
        // This mimics the response we'd get from the API
        return listOf(
            Candidate(
                name = "Diana López",
                role = "Developer",
                overallMatch = 95,
                skills = listOf(
                    SkillMatch("React", 95),
                    SkillMatch("TypeScript", 88),
                    SkillMatch("Leadership", 75)
                )
            ),
            Candidate(
                name = "Carlos Ruiz",
                role = "Engineer",
                overallMatch = 78,
                skills = listOf(
                    SkillMatch("React", 70),
                    SkillMatch("TypeScript", 65),
                    SkillMatch("Leadership", 90)
                )
            )
        )
    }
}