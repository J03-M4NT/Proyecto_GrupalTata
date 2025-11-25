package com.example.proyecto_grupaltata.presentation.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.domain.model.Collaborator
import com.example.proyecto_grupaltata.presentation.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    var collaborator by remember { mutableStateOf<Collaborator?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(key1 = navController.currentBackStackEntry) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("ProfileScreen", "UserID is null, cannot fetch profile.")
            isLoading = false
            return@LaunchedEffect
        }

        isLoading = true
        FirebaseFirestore.getInstance().collection("collaborators").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    collaborator = document.toObject(Collaborator::class.java)?.apply {
                        this.userId = document.id // Asignar el ID del documento al objeto
                    }
                } else {
                    collaborator = null // Si no existe, el colaborador es nulo
                }
                isLoading = false
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileScreen", "Error al cargar el perfil", exception)
                Toast.makeText(context, "Error al cargar perfil.", Toast.LENGTH_SHORT).show()
                collaborator = null
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (collaborator != null) {
                        IconButton(onClick = { navController.navigate(AppScreens.EditProfileScreen.route) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar Perfil")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (collaborator != null) {
            collaborator?.let { user ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    ProfileDetailRow(label = "Nombre", value = user.nombre)
                    ProfileDetailRow(label = "Rol", value = user.rol)
                    ProfileDetailRow(label = "Nivel", value = user.nivel)
                    ProfileDetailRow(label = "Proyecto", value = user.project)
                    ProfileDetailRow(label = "Disponibilidad", value = if (user.movilidad) "Disponible" else "No Disponible")

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Skills", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (user.skills.isNotEmpty()) {
                        user.skills.forEach { skill ->
                            ProfileDetailRow(label = "Skill", value = skill)
                        }
                    } else {
                        Text("Aún no hay skills registrados.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                }
            }
        } else {
            LaunchedEffect(Unit) {
                navController.navigate(AppScreens.EditProfileScreen.route)
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}
