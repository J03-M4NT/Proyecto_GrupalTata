package com.example.proyecto_grupaltata.presentation.matching

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.model.Colaborador
import com.example.proyecto_grupaltata.model.Skill
import com.example.proyecto_grupaltata.presentation.mapping.MappingUiState
import com.example.proyecto_grupaltata.presentation.mapping.MappingViewModel
import com.example.proyecto_grupaltata.ui.theme.BlueTCS
import com.example.proyecto_grupaltata.ui.theme.OrangeTcs
import java.util.UUID

@Composable
fun MatchingScreen(
    navController: NavController,
    mappingViewModel: MappingViewModel = viewModel()
) {
    val searchText by mappingViewModel.searchText.collectAsState()
    val collaborators by mappingViewModel.filteredCollaborators.collectAsState(emptyList())
    val uiState = mappingViewModel.uiState.value
    // --- NUEVO: Obtenemos el colaborador seleccionado del ViewModel ---
    val selectedCollaborator by mappingViewModel.selectedCollaborator.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        Text("Skill Mapping", style = MaterialTheme.typography.headlineMedium, color = OrangeTcs, fontWeight = FontWeight.Bold)
        Text("Inventario de habilidades de colaboradores", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = mappingViewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar colaborador...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is MappingUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MappingUiState.Success -> {
                if (collaborators.isEmpty()) {
                     Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val message = if (searchText.isNotEmpty()) "No se encontraron colaboradores." else "No hay colaboradores registrados."
                        Text(text = message)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(collaborators, key = { it.id ?: UUID.randomUUID() }) { collaborator ->
                            // --- MODIFICADO: Hacemos la tarjeta clicable ---
                            CollaboratorCard(
                                collaborator = collaborator,
                                onClick = { mappingViewModel.onCollaboratorSelected(collaborator) }
                            )
                        }
                    }
                }
            }
            is MappingUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    // --- NUEVO: Mostramos el diálogo si hay un colaborador seleccionado ---
    selectedCollaborator?.let { collaborator ->
        SkillsDialog(
            collaborator = collaborator,
            onDismiss = { mappingViewModel.onDialogDismiss() }
        )
    }
}

// --- MODIFICADO: Añadimos el parámetro onClick ---
@Composable
private fun CollaboratorCard(collaborator: Colaborador, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(BlueTCS),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (collaborator.nombre).getInitials(),
                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = collaborator.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = collaborator.puesto, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val techSkills = collaborator.technicalSkills.size
                        val softSkills = collaborator.softSkills.size
                        if (techSkills > 0) SkillChip(text = "$techSkills Tech", color = OrangeTcs.copy(alpha = 0.2f))
                        if (softSkills > 0) SkillChip(text = "$softSkills Soft", color = BlueTCS.copy(alpha = 0.2f))
                    }
                }
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = "Ver detalles", tint = Color.Gray)
        }
    }
}

// --- NUEVO: Diálogo de Habilidades ---
@Composable
fun SkillsDialog(collaborator: Colaborador, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(collaborator.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(collaborator.puesto, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))

                Text("Technical Skills", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                collaborator.technicalSkills.forEach { skill -> SkillProgressBar(skill = skill, color = BlueTCS) }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Soft Skills", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                collaborator.softSkills.forEach { skill -> SkillProgressBar(skill = skill, color = OrangeTcs) }

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("Cerrar")
                }
            }
        }
    }
}

// --- NUEVO: Barra de Progreso de Habilidad ---
@Composable
fun SkillProgressBar(skill: Skill, color: Color) {
    val percentageFloat = skill.percentage / 100f
    val animatedProgress by animateFloatAsState(targetValue = percentageFloat, label = "progressAnimation")

    Column(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = skill.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "${skill.percentage}%", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.background(color.copy(alpha = 0.2f)).fillMaxWidth().height(10.dp).clip(CircleShape)) {
            Box(
                modifier = Modifier
                    .background(color)
                    .fillMaxWidth(animatedProgress)
                    .height(10.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
private fun SkillChip(text: String, color: Color) {
    Surface(shape = RoundedCornerShape(8.dp), color = color) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

fun String.getInitials(): String {
    return this.split(' ')
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first() }
        .joinToString("")
        .uppercase()
}