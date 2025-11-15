package com.example.proyecto_grupaltata.data

import com.example.proyecto_grupaltata.model.Colaborador
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ColaboradorRepository {

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("colaboradores")

    // US-003: Crear colaborador
    suspend fun agregar(col: Colaborador) {
        val doc = ref.add(col).await()
        ref.document(doc.id).update("id", doc.id)
    }

    // US-006: Obtener todos
    suspend fun obtenerTodos(): List<Colaborador> {
        val snapshot = ref.get().await()
        return snapshot.toObjects(Colaborador::class.java)
    }

    // US-011: Actualizar skills y nivel
    suspend fun actualizarSkills(id: String, skills: List<String>, nivel: String) {
        ref.document(id).update(
            mapOf(
                "skills" to skills,
                "nivel" to nivel,
                "fechaActualizacion" to System.currentTimeMillis()
            )
        ).await()
    }
}
