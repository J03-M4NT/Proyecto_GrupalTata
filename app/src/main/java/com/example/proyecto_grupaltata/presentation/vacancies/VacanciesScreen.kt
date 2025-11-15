package com.example.proyecto_grupaltata.presentation.vacancies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.domain.model.Vacancy
import com.example.proyecto_grupaltata.presentation.register_vacancy.RegisterVacancyDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacanciesScreen(navController: NavController) {

    var showRegisterDialog by remember { mutableStateOf(false) }
    val vacancies = remember { mutableStateListOf<Vacancy>() }

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

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar vacante...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (vacancies.isEmpty()) {
                Text(
                    "Aún no hay vacantes registradas.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(vacancies) { vacancy ->
                        VacancyCard(vacancy = vacancy) {
                            // Navigate to matching screen when a card is clicked
                            val skillsRoute = vacancy.requiredSkills.joinToString(",")
                            navController.navigate("matching/$skillsRoute")
                        }
                    }
                }
            }
        }
    }

    if (showRegisterDialog) {
        RegisterVacancyDialog(
            onDismissRequest = { showRegisterDialog = false },
            onVacancyRegistered = { newVacancy ->
                vacancies.add(newVacancy)
                showRegisterDialog = false
                // Navigation is no longer here
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyCard(vacancy: Vacancy, onClick: () -> Unit) { // Added onClick lambda
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), // Made the whole card clickable
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = vacancy.profileName,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                // Placeholder for status
                Text(
                    text = "Abierta", 
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF4CAF50), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
            Text(text = "IT", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                vacancy.requiredSkills.take(3).forEach { skill ->
                    AssistChip(onClick = {}, label = { Text(skill) })
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.People, contentDescription = "Candidatos", tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "0 candidatos", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}