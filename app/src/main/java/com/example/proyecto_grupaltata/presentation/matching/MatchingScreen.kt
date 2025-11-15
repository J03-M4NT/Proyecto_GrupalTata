package com.example.proyecto_grupaltata.presentation.matching

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.domain.model.Candidate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchingScreen(
    navController: NavController,
    skills: List<String>,
    matchingViewModel: MatchingViewModel = viewModel()
) {

    // Trigger the search when the screen is first composed
    LaunchedEffect(key1 = skills) {
        matchingViewModel.findCandidates(skills)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Candidatos Encontrados") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            when (val state = matchingViewModel.uiState.value) {
                is MatchingUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is MatchingUiState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.candidates) { candidate ->
                            CandidateCard(candidate = candidate)
                        }
                    }
                }
                is MatchingUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun CandidateCard(candidate: Candidate) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = candidate.name, style = MaterialTheme.typography.titleLarge)
                Text(text = "${candidate.overallMatch}% Match", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
            }
            Text(text = candidate.role, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            
            candidate.skills.forEach { skill ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = skill.name)
                    Text(text = "${skill.percentage}%")
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}