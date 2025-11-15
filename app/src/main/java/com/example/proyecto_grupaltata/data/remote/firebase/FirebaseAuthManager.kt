package com.example.proyecto_grupaltata.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


object FirebaseAuthManager {

    // Conexion de las credenciales con la Autenticacion de Firebase
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Funcion - Registrarse(?


    // Funcion - Iniciar Sesion
    suspend fun loginuser(email: String, password: String): Result<Unit> {

        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }


    // Funcion - Cerrar Sesion
    fun logout(){
        auth.signOut()
    }


}


