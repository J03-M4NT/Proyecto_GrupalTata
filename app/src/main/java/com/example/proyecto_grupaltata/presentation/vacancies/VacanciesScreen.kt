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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.model.Colaborador
import com.example.proyecto_grupaltata.domain.model.Vacancy
import com.example.proyecto_grupaltata.presentation.mapping.MappingViewModel
import com.example.proyecto_grupaltata.presentation.register_vacancy.RegisterVacancyDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacanciesScreen(
    navController: NavController,
    vacanciesViewModel: VacanciesViewModel = viewModel(),
    mappingViewModel: MappingViewModel = viewModel() // Shared ViewModel
) {

    var showRegisterDialog by remember { mutableStateOf(false) }
    var selectedVacancy by remember { mutableStateOf<Vacancy?>(null) }

    val searchQuery = vacanciesViewModel.searchQuery
    val allVacancies by vacanciesViewModel.vacancies.collectAsState()
    val collaborators by mappingViewModel.filteredCollaborators.collectAsState(initial = emptyList())

    val filteredVacancies = remember(allVacancies, searchQuery) {
        if (searchQuery.isBlank()) allVacancies else allVacancies.filter {
            it.profileName.contains(searchQuery, ignoreCase = true)
        }
    }

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
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Vacantes y candidatos internos", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { vacanciesViewModel.onSearchQueryChange(it) },
                placeholder = { Text("Buscar vacante...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredVacancies.isEmpty()) {
                Text(
                    text = if (searchQuery.isBlank()) "Aún no hay vacantes registradas." else "No se encontraron vacantes.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredVacancies) { vacancy ->
                        VacancyCard(
                            vacancy = vacancy,
                            collaborators = collaborators, // Pass the list of collaborators
                            onClick = { selectedVacancy = vacancy }
                        )
                    }
                }
            }
        }
    }

    if (showRegisterDialog) {
        RegisterVacancyDialog(
            onDismissRequest = { showRegisterDialog = false },
            onVacancyRegistered = { newVacancy ->
                vacanciesViewModel.addVacancy(newVacancy)
                showRegisterDialog = false
            }
        )
    }

    selectedVacancy?.let {
        vacancy ->
        MatchDialog(
            vacancy = vacancy,
            collaborators = collaborators,
            onDismiss = { selectedVacancy = null }
        )
    }
}

@Composable
private fun MatchDialog(vacancy: Vacancy, collaborators: List<Colaborador>, onDismiss: () -> Unit) {
    val matchingCollaborators = remember(vacancy, collaborators) {
        if (vacancy.requiredSkills.isEmpty()) emptyList() else collaborators.filter {
            collaborator ->
            val userSkills = collaborator.technicalSkills.map { it.name } + collaborator.softSkills.map { it.name }
            vacancy.requiredSkills.any { requiredSkill -> userSkills.any { it.equals(requiredSkill, ignoreCase = true) } }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                Text(text = "Match para: ${vacancy.profileName}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                if (matchingCollaborators.isEmpty()) {
                    Text("No se encontraron colaboradores para esta vacante.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(matchingCollaborators) { collaborator ->
                            Text(collaborator.nombre, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyCard(vacancy: Vacancy, collaborators: List<Colaborador>, onClick: () -> Unit) {
    val matchingCollaboratorsCount = remember(vacancy, collaborators) {
        if (vacancy.requiredSkills.isEmpty()) 0 else collaborators.count {
            collaborator ->
            val userSkills = collaborator.technicalSkills.map { it.name } + collaborator.softSkills.map { it.name }
            vacancy.requiredSkills.any { requiredSkill -> userSkills.any { it.equals(requiredSkill, ignoreCase = true) } }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = vacancy.profileName,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Abierta", 
                    color = Color.White,
                    modifier = Modifier.background(Color(0xFF4CAF50), RoundedCornerShape(50)).padding(horizontal = 12.dp, vertical = 4.dp)
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
                Text(
                    text = "$matchingCollaboratorsCount candidatos",
                    color = Color.Gray, 
                    fontSize = 14.sp
                )
            }
        }
    }
}