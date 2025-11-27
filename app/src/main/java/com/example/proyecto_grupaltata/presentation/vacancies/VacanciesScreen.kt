package com.example.proyecto_grupaltata.presentation.vacancies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.model.Colaborador
import com.example.proyecto_grupaltata.domain.model.Vacancy
import com.example.proyecto_grupaltata.model.Skill
import com.example.proyecto_grupaltata.presentation.mapping.MappingViewModel
import com.example.proyecto_grupaltata.presentation.register_vacancy.RegisterVacancyDialog
import androidx.compose.foundation.BorderStroke
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacanciesScreen(
    navController: NavController,
    vacanciesViewModel: VacanciesViewModel = viewModel(),
    mappingViewModel: MappingViewModel = viewModel() // Shared ViewModel
) {

    var showRegisterDialog by remember { mutableStateOf(false) }
    var selectedVacancy by remember { mutableStateOf<Vacancy?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val searchQuery = vacanciesViewModel.searchQuery
    val allVacancies by vacanciesViewModel.vacancies.collectAsState()
    val collaborators by mappingViewModel.filteredCollaborators.collectAsState(initial = emptyList())

    val filteredVacancies = remember(allVacancies, searchQuery) {
        if (searchQuery.isBlank()) allVacancies else allVacancies.filter {
            it.profileName.contains(searchQuery, ignoreCase = true)
        }
    }

    // Show the bottom sheet if a vacancy is selected
    LaunchedEffect(selectedVacancy) {
        if (selectedVacancy != null) {
            scope.launch { sheetState.show() }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Talent Demand",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFF57C00)
            )
            IconButton(onClick = { showRegisterDialog = true }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Registrar nueva vacante",
                    tint = Color(0xFF1976D2)
                )
            }
        }

        Text("Vacantes y candidatos internos", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
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
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (searchQuery.isBlank()) "Aún no hay vacantes registradas." else "No se encontraron vacantes.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredVacancies) { vacancy ->
                    VacancyCard(
                        vacancy = vacancy,
                        collaborators = collaborators,
                        onClick = { selectedVacancy = vacancy }
                    )
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

    // ModalBottomSheet replaces the Dialog
    if (selectedVacancy != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedVacancy = null },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            MatchSheetContent(
                vacancy = selectedVacancy!!,
                collaborators = collaborators,
                onDismiss = { 
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion { 
                        if (!sheetState.isVisible) {
                            selectedVacancy = null
                        }
                    } 
                }
            )
        }
    }
}

@Composable
private fun MatchSheetContent(vacancy: Vacancy, collaborators: List<Colaborador>, onDismiss: () -> Unit) {
    val allMatchingCollaborators = remember(vacancy, collaborators) {
        if (vacancy.requiredSkills.isEmpty()) emptyList() else collaborators.filter {
            collaborator ->
            val userSkills = (collaborator.technicalSkills + collaborator.softSkills).map { it.name.lowercase() }
            vacancy.requiredSkills.any { requiredSkill -> userSkills.contains(requiredSkill.lowercase()) }
        }
    }

    val statusText = if (allMatchingCollaborators.isNotEmpty()) "Abierta" else "Externa"
    val statusColor = if (allMatchingCollaborators.isNotEmpty()) Color(0xFF4CAF50) else Color(0xFFF44336)

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vacancy.profileName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("IT", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = statusText,
                    color = Color.White,
                    modifier = Modifier.background(statusColor, RoundedCornerShape(50)).padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Skills Requeridos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            vacancy.requiredSkills.forEach { skillName ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = skillName,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Text("Candidatos Internos (${allMatchingCollaborators.size})")
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (allMatchingCollaborators.isEmpty()) {
                item {
                    Text("No se encontraron colaboradores para esta vacante.")
                }
            } else {
                items(allMatchingCollaborators) { collaborator ->
                    CandidateMatchCard(collaborator = collaborator, requiredSkills = vacancy.requiredSkills)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun CandidateMatchCard(collaborator: Colaborador, requiredSkills: List<String>) {
    val collaboratorSkills = (collaborator.technicalSkills + collaborator.softSkills).associate { it.name.lowercase() to it.percentage }
    val matchedSkills = requiredSkills.mapNotNull { requiredSkill ->
        collaboratorSkills[requiredSkill.lowercase()]?.let { percentage ->
            Skill(name = requiredSkill, percentage = percentage)
        }
    }

    val overallMatch = if (matchedSkills.isEmpty()) 0 else matchedSkills.sumOf { it.percentage } / matchedSkills.size

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(getInitials(collaborator.nombre), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(collaborator.nombre, fontWeight = FontWeight.Bold)
                Text(collaborator.puesto, color = Color.Gray)
            }
            Text("$overallMatch% Match", color = if (overallMatch > 80) Color(0xFF388E3C) else Color.Gray, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier.padding(start = 52.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            matchedSkills.forEach { skill ->
                Text(buildAnnotatedString {
                    append("• ${skill.name} ")
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("(${skill.percentage}%)")
                    }
                })
            }
        }
    }
}

private fun getInitials(name: String): String {
    return name.split(' ').filter { it.isNotBlank() }.take(2).map { it.first() }.joinToString("").uppercase()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyCard(vacancy: Vacancy, collaborators: List<Colaborador>, onClick: () -> Unit) {
    val matchingCollaboratorsCount = remember(vacancy, collaborators) {
        if (vacancy.requiredSkills.isEmpty()) 0 else collaborators.count {
            collaborator ->
            val userSkills = (collaborator.technicalSkills + collaborator.softSkills).map { it.name.lowercase() }
            vacancy.requiredSkills.any { requiredSkill -> userSkills.contains(requiredSkill.lowercase()) }
        }
    }

    val statusText = if (matchingCollaboratorsCount > 0) "Abierta" else "Externa"
    val statusColor = if (matchingCollaboratorsCount > 0) Color(0xFF4CAF50) else Color(0xFFF44336)

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = vacancy.profileName,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1976D2)
                )
                Text(
                    text = statusText,
                    color = Color.White,
                    modifier = Modifier.background(statusColor, RoundedCornerShape(50)).padding(horizontal = 12.dp, vertical = 4.dp)
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