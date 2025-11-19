package com.example.proyecto_grupaltata.presentation.matching

import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.model.Colaborador
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



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        // --- Header ---
        Text(
            text = "Skill Mapping",
            style = MaterialTheme.typography.headlineMedium,
            color = OrangeTcs,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Inventario de habilidades de colaboradores",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Search Bar ---
        OutlinedTextField(
            value = searchText,
            onValueChange = mappingViewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar colaborador...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Content ---
        when (uiState) {
            is MappingUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MappingUiState.Success -> {
                if (collaborators.isEmpty()) {
                     Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val message = if (searchText.isNotEmpty()) {
                            "No se encontraron colaboradores."
                        } else {
                            "No hay colaboradores registrados."
                        }
                        Text(text = message)
                    }
                } else {
                    // ¡CORRECCIÓN 2! Se cambia fillMaxSize por weight(1f) para que la lista sea visible
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        // ¡CORRECCIÓN 1! Se provee un key no nulo para evitar el error
                        items(collaborators, key = { it.id ?: UUID.randomUUID() }) { collaborator ->
                            CollaboratorCard(collaborator = collaborator)
                        }
                    }
                }
            }
            is MappingUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Composable
private fun CollaboratorCard(collaborator: Colaborador) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { /* TODO: Navigate to collaborator detail screen */ }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // --- Avatar with Initials ---
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(BlueTCS),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (collaborator.nombre ?: "").getInitials(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // --- Name, Role and Skills ---
                Column {
                    Text(text = collaborator.nombre ?: "Sin nombre", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = collaborator.puesto ?: "Sin puesto", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val techSkills = collaborator.technicalSkills?.size ?: 0
                        val softSkills = collaborator.softSkills?.size ?: 0

                        if (techSkills > 0) {
                            SkillChip(text = "$techSkills Tech", color = OrangeTcs.copy(alpha = 0.2f))
                        }
                        if (softSkills > 0) {
                            SkillChip(text = "$softSkills Soft", color = BlueTCS.copy(alpha = 0.2f))
                        }
                    }
                }
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = "Ver detalles", tint = Color.Gray)
        }
    }
}

@Composable
private fun SkillChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color,
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

// Función de extensión para obtener las iniciales
fun String.getInitials(): String {
    return this.split(' ')
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first() }
        .joinToString("")
        .uppercase()
}
