package com.example.proyecto_grupaltata.presentation.brechas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BrechasViewModel : ViewModel() {
    private val _requiredSkillsCount = MutableStateFlow(0)
    val requiredSkillsCount: StateFlow<Int> = _requiredSkillsCount

    private val _availableSkillsCount = MutableStateFlow(0)
    val availableSkillsCount: StateFlow<Int> = _availableSkillsCount

    init {
        fetchRequiredSkillsCount()
        fetchAvailableSkillsCount()
    }

    fun fetchRequiredSkillsCount() {
        viewModelScope.launch {
            val db = Firebase.firestore
            db.collection("vacantes")
                .get()
                .addOnSuccessListener { result ->
                    var totalSkills = 0
                    for (document in result) {
                        val skills = document.get("requiredSkills")
                        if (skills is List<*>) {
                            totalSkills += skills.size
                        }
                    }
                    _requiredSkillsCount.value = totalSkills
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }

    fun fetchAvailableSkillsCount() {
        viewModelScope.launch {
            val db = Firebase.firestore
            db.collection("collaborators")
                .get()
                .addOnSuccessListener { result ->
                    var totalSkills = 0
                    for (document in result) {
                        val skills = document.get("technicalSkills")
                        if (skills is List<*>) {
                            totalSkills += skills.size
                        }
                    }
                    _availableSkillsCount.value = totalSkills
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }
}

@Composable
fun BrechasScreen(viewModel: BrechasViewModel = viewModel()) {
    val requiredSkillsCount by viewModel.requiredSkillsCount.collectAsState()
    val availableSkillsCount by viewModel.availableSkillsCount.collectAsState()

    val gap = (requiredSkillsCount - availableSkillsCount).coerceAtLeast(0)

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Skills Gaps", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Análisis de brechas de habilidades", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SkillGapCard(requiredSkillsCount.toString(), "Requeridos", Icons.AutoMirrored.Filled.TrendingUp, Color.Blue)
                SkillGapCard(availableSkillsCount.toString(), "Disponibles", Icons.Default.People, Color.Green)
                SkillGapCard(gap.toString(), "Brecha", Icons.Default.Info, Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Brechas por Skill", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    // Placeholder for the bar chart
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.LightGray.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Bar Chart Placeholder")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (gap > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "Acción Requerida", tint = Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Acción Requerida", fontWeight = FontWeight.Bold, color = Color.Red)
                            Text("$gap brechas de alta prioridad identificadas. Se recomienda capacitación interna o reclutamiento externo.", fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = "Detalle de Brechas")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Detalle de Brechas", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            // Add details here
        }
    }
}

@Composable
fun SkillGapCard(value: String, label: String, icon: ImageVector, iconColor: Color) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = label, tint = iconColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(label, fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}
