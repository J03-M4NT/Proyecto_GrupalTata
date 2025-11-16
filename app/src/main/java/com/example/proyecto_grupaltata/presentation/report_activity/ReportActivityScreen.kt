
package com.example.proyecto_grupaltata.presentation.report_activity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Data class para modelar cada reporte
data class ReportData(
    val title: String,
    val date: String,
    val recordCount: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportActivityScreen(navController: NavController) {

    // Datos de ejemplo basados en tu diseño de Figma
    val dummyReports = listOf(
        ReportData("Reporte de Skills por Departamento", "13 nov 2023", 127),
        ReportData("Reporte de Vacantes Cubiertas", "09 nov 2023", 8),
        ReportData("Reporte de Movilidad Interna", "04 nov 2023", 15),
        ReportData("Reporte de Brechas de Skills", "31 oct 2023", 42),
        ReportData("Reporte Mensual de Actividad", "30 oct 2023", 234)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportes de Actividad") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabecera con resumen
            item {
                Column {
                    Text(
                        text = "Exporta y descarga reportes desde Firebase Storage",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SummaryCard(
                            title = "Total Reportes",
                            value = "5",
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE8EAF6) // Azul claro
                        )
                        SummaryCard(
                            title = "Registros",
                            value = "426",
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE8F5E9) // Verde claro
                        )
                    }
                }
            }

            // Lista de reportes
            items(dummyReports) { report ->
                ReportListItem(report = report)
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ReportListItem(report: ReportData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título y fecha del reporte
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Icono de reporte",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = report.title, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${report.date} • ${report.recordCount} registros",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Botones de descarga
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Descargar PDF
                OutlinedButton(
                    onClick = { /* Lógica de descarga de PDF */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Descargar PDF", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Descargar PDF")
                }
                // Botón Descargar CSV
                Button(
                    onClick = { /* Lógica de descarga de CSV */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50) // Verde
                    )
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Descargar CSV", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Descargar CSV")
                }
            }
        }
    }
}
