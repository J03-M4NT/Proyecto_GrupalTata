package com.example.proyecto_grupaltata.presentation.vacancies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto_grupaltata.presentation.register_vacancy.RegisterVacancyDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacanciesScreen() { // navController removed

    var showRegisterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Talent Demand") },
                actions = {
                    IconButton(onClick = { showRegisterDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Registrar nueva vacante")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Vacantes y candidatos internos", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar Placeholder
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar vacante...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Vacancy List Placeholder
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { 
                    Text("Aquí irá la lista de vacantes...", 
                         style = MaterialTheme.typography.bodyLarge, 
                         color = Color.Gray,
                         modifier = Modifier.padding(16.dp)
                    )
                } 
            }
        }
    }

    if (showRegisterDialog) {
        RegisterVacancyDialog(onDismissRequest = { showRegisterDialog = false })
    }
}