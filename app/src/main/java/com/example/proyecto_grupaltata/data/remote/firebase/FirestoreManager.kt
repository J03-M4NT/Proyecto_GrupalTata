package com.example.proyecto_grupaltata.data.remote.firebase

import com.example.proyecto_grupaltata.domain.model.Vacancy
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreManager {

    private val firestore = FirebaseFirestore.getInstance()
    private val vacanciesCollection = firestore.collection("vacantes")

    suspend fun addVacancy(vacancy: Vacancy) {
        try {
            vacanciesCollection.document(vacancy.id).set(vacancy).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVacanciesFlow(): Flow<List<Vacancy>> = callbackFlow {
        val subscription = vacanciesCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val vacancies = snapshot.toObjects(Vacancy::class.java)
                    trySend(vacancies).isSuccess
                }
            }

        awaitClose { subscription.remove() }
    }
}