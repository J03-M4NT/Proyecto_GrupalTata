package com.example.proyecto_grupaltata.presentation.colaborador

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddColaboradorScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var puesto by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Añadir Nuevo Colaborador", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del colaborador") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = puesto,
            onValueChange = { puesto = it },
            label = { Text("Puesto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && puesto.isNotBlank()) {
                    // AQUÍ VA LA LÓGICA PARA GUARDAR EN FIREBASE
                    // Por ahora, solo mostramos un mensaje y volvemos atrás.
                    Toast.makeText(context, "Colaborador guardado (simulación)", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // Regresa a la pantalla anterior (HomeScreen)
                } else {
                    Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Colaborador")
        }
    }
}
