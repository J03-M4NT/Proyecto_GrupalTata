package com.example.proyecto_grupaltata.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.presentation.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pantalla Principal") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        // Botón flotante para añadir colaborador
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Acción para navegar a la pantalla de agregar colaborador
                navController.navigate(AppScreens.AddColaboradorScreen.route)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Colaborador")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("¡Bienvenido! Usa el botón '+' para agregar un colaborador.")
            // Aquí podrías mostrar la lista de colaboradores desde Firebase
        }
    }
}
