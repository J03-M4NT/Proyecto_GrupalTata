package com.example.proyecto_grupaltata.presentation.brechas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
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
import kotlinx.coroutines.tasks.await
import kotlin.math.ceil

// Data class to hold the details for each skill gap
data class SkillGapDetail(
    val skill: String,
    val required: Int,
    val available: Int,
    val gap: Int,
    val coverage: Float,
    val priority: String // "Alta", "Media", "Baja"
)

class BrechasViewModel : ViewModel() {
    private val _requiredSkillsCount = MutableStateFlow(0)
    val requiredSkillsCount: StateFlow<Int> = _requiredSkillsCount

    private val _availableSkillsCount = MutableStateFlow(0)
    val availableSkillsCount: StateFlow<Int> = _availableSkillsCount

    private val _skillGapDetails = MutableStateFlow<List<SkillGapDetail>>(emptyList())
    val skillGapDetails: StateFlow<List<SkillGapDetail>> = _skillGapDetails


    init {
        fetchRequiredSkillsCount()
        fetchAvailableSkillsCount()
        fetchSkillGapDetails()
    }

    fun fetchRequiredSkillsCount() {
        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                val result = db.collection("vacantes").get().await()
                var totalSkills = 0
                for (document in result) {
                    val skills = document.get("requiredSkills")
                    if (skills is List<*>) {
                        totalSkills += skills.size
                    }
                }
                _requiredSkillsCount.value = totalSkills
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchAvailableSkillsCount() {
        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                val result = db.collection("collaborators").get().await()
                var totalSkills = 0
                for (document in result) {
                    val skills = document.get("technicalSkills")
                    if (skills is List<*>) {
                        totalSkills += skills.size
                    }
                }
                _availableSkillsCount.value = totalSkills
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchSkillGapDetails() {
        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                val requiredSkillsMap = mutableMapOf<String, Int>()
                val availableSkillsMap = mutableMapOf<String, Int>()

                // 1. Fetch and process required skills from 'vacantes'
                val vacantesSnapshot = db.collection("vacantes").get().await()
                val requiredSkillsSet = mutableSetOf<String>()
                for (document in vacantesSnapshot) {
                    val skills = document.get("requiredSkills") as? List<*>
                    skills?.forEach { skill ->
                        if (skill is String) {
                            requiredSkillsSet.add(skill)
                            requiredSkillsMap[skill] = (requiredSkillsMap[skill] ?: 0) + 1
                        }
                    }
                }

                // 2. Fetch and process available skills from 'collaborators'
                val collaboratorsSnapshot = db.collection("collaborators").get().await()
                for (document in collaboratorsSnapshot) {
                    val skillsList = document.get("technicalSkills") as? List<Map<String, Any>>
                    skillsList?.forEach { skillMap ->
                        val skillName = skillMap["name"] as? String
                        if (skillName != null) {
                            availableSkillsMap[skillName] = (availableSkillsMap[skillName] ?: 0) + 1
                        }
                    }
                }

                // 3. Combine data and calculate details for required skills only
                val detailsList = requiredSkillsSet.map { skill ->
                    val required = requiredSkillsMap[skill] ?: 0
                    val available = availableSkillsMap[skill] ?: 0
                    val gap = (required - available).coerceAtLeast(0)
                    val coverage = if (required > 0) (available.toFloat() / required.toFloat()) else 1f
                    val priority = when {
                        coverage <= 0.5f -> "Alta"
                        coverage <= 0.8f -> "Media"
                        else -> "Baja"
                    }
                    SkillGapDetail(skill, required, available, gap, coverage, priority)
                }.sortedByDescending { it.gap }

                _skillGapDetails.value = detailsList

            } catch (e: Exception) {
                // Handle exceptions like network errors
            }
        }
    }
}

@Composable
fun BrechasScreen(viewModel: BrechasViewModel = viewModel()) {
    val requiredSkillsCount by viewModel.requiredSkillsCount.collectAsState()
    val availableSkillsCount by viewModel.availableSkillsCount.collectAsState()
    val skillGapDetails by viewModel.skillGapDetails.collectAsState()

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
                    if (skillGapDetails.isNotEmpty()) {
                        SkillBarChart(skillGapDetails = skillGapDetails.take(5)) // Show top 5 gaps
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.LightGray.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No data available for chart")
                        }
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
            Spacer(modifier = Modifier.height(8.dp))

            // Details section
            Column {
                if (skillGapDetails.isEmpty()) {
                    Text("Calculando detalles...", color = Color.Gray)
                } else {
                    skillGapDetails.forEach { detail ->
                        SkillGapDetailCard(detail = detail)
                    }
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SkillBarChart(skillGapDetails: List<SkillGapDetail>) {
    val dataMaxValue = skillGapDetails.maxOfOrNull { it.gap } ?: 0
    val chartColor = Color(0xFFF44336) // Red color for bars

    if (dataMaxValue == 0) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No gap data to display.")
        }
        return
    }

    val numGridLines = 4
    val step = ceil(dataMaxValue.toFloat() / numGridLines).toInt().coerceAtLeast(1)
    val axisMaxValue = step * numGridLines

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)
        .padding(16.dp)) {
        val barWidth = size.width / (skillGapDetails.size * 2)
        val spaceBetweenBars = barWidth
        val maxBarHeight = size.height * 0.8f

        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 12.sp.toPx()
            color = android.graphics.Color.BLACK
        }

        // Draw Y-axis labels and grid lines
        for (i in 0..numGridLines) {
            val value = i * step
            val y = size.height - (i.toFloat() / numGridLines) * maxBarHeight

            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    value.toString(),
                    -10f,
                    y + 5f,
                    textPaint
                )
            }
            if (i > 0) {
                drawLine(
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    color = Color.LightGray,
                    strokeWidth = 1f
                )
            }
        }

        // Draw bars and X-axis labels
        skillGapDetails.forEachIndexed { index, detail ->
            val barHeight = (detail.gap.toFloat() / axisMaxValue) * maxBarHeight
            val left = (index * (barWidth + spaceBetweenBars)) + spaceBetweenBars / 2
            val top = size.height - barHeight

            // Draw bar
            drawRect(
                color = chartColor,
                topLeft = Offset(left, top),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )

            // Draw X-axis label
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    detail.skill,
                    left + barWidth / 2 - (textPaint.measureText(detail.skill) / 2),
                    size.height + 15.sp.toPx(),
                    textPaint
                )
            }
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

@Composable
fun SkillGapDetailCard(detail: SkillGapDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Skill name and stats
            Column(modifier = Modifier.weight(1f)) {
                Text(detail.skill, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text("Req: ${detail.required}", fontSize = 14.sp, color = Color.Gray)
                    Text(" • ", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(horizontal = 4.dp))
                    Text("Disp: ${detail.available}", fontSize = 14.sp, color = Color.Gray)
                    Text(" • ", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(horizontal = 4.dp))
                    Text("Gap: ${detail.gap}", fontSize = 14.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }

            // Right side: Priority and Coverage
            Column(horizontalAlignment = Alignment.End) {
                val (priorityColor, priorityText) = when (detail.priority) {
                    "Alta" -> Pair(Color.Red, "Alta")
                    "Media" -> Pair(Color(0xFFF57C00), "Media") // Orange
                    else -> Pair(Color(0xFF388E3C), "Baja") // Green
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(color = priorityColor, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = priorityText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${(detail.coverage * 100).toInt()}%",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Text("Cobertura", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
