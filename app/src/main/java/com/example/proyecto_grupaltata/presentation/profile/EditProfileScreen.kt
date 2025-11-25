package com.example.proyecto_grupaltata.presentation.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// **CORRECCIÓN 1: Recibir el userId como parámetro**
fun EditProfileScreen(navController: NavController, userId: String) {

    var nombre by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var nivel by remember { mutableStateOf("") }
    var project by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var isNewProfile by remember { mutableStateOf(true) }

    val context = LocalContext.current

    // **CORRECCIÓN 2: Usar el userId recibido directamente**
    LaunchedEffect(key1 = userId) {
        FirebaseFirestore.getInstance().collection("collaborators").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    nombre = document.getString("nombre") ?: ""
                    rol = document.getString("rol") ?: ""
                    nivel = document.getString("nivel") ?: ""
                    project = document.getString("project") ?: ""
                    isNewProfile = false
                } else {
                    isNewProfile = true
                }
                isLoading = false
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNewProfile) "Crear Perfil" else "Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = rol, onValueChange = { rol = it }, label = { Text("Rol") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = nivel, onValueChange = { nivel = it }, label = { Text("Nivel") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = project, onValueChange = { project = it }, label = { Text("Proyecto / Cuenta") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { handleSaveProfile(userId, nombre, rol, nivel, project, context, navController) { isSaving = it } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSaving
                ) {
                    if (isSaving) { CircularProgressIndicator() } else { Text("Guardar Cambios") }
                }
            }
        }
    }
}

private fun handleSaveProfile(
    userId: String, nombre: String, rol: String, nivel: String, project: String,
    context: android.content.Context, navController: NavController, onSavingStateChange: (Boolean) -> Unit
) {
    if (nombre.isBlank() || rol.isBlank() || nivel.isBlank()) {
        Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show()
        return
    }
    onSavingStateChange(true)
    val profileData = hashMapOf(
        "userId" to userId,
        "nombre" to nombre,
        "rol" to rol,
        "nivel" to nivel,
        "project" to project,
        "fechaActualizacion" to FieldValue.serverTimestamp()
    )
    FirebaseFirestore.getInstance().collection("collaborators").document(userId)
        .set(profileData, SetOptions.merge())
        .addOnSuccessListener {
            onSavingStateChange(false)
            Toast.makeText(context, "Perfil guardado con éxito", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            onSavingStateChange(false)
            Log.e("EditProfileScreen", "Error al guardar perfil", e)
            Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
        }
}
