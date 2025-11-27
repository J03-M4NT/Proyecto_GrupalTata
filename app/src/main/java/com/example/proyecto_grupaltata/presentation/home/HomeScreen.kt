package com.example.proyecto_grupaltata.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // The TopAppBar is now handled by the main Scaffold in MainActivity

        // Texto de Bienvenida
        Text("¡Bienvenido!", style = MaterialTheme.typography.headlineMedium, color = Color.Gray)
        Text(
            "Sistema de Gestión de Talento Interno",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Info Cards Grid
        DashboardGrid()

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Activity
        Text(
            "Actividad Reciente",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        RecentActivityList()
    }
}


@Composable
fun DashboardGrid() {
    val items = listOf(
        InfoCardData("Colaboradores", "127", Icons.Default.People, Color(0xFF4A86E8)),
        InfoCardData("Vacantes Abiertas", "4", Icons.Default.Work, Color(0xFFF57C00)),
        InfoCardData("Skills Mapeados", "245", Icons.Default.Checklist, Color(0xFF388E3C)),
        InfoCardData("Match Interno", "85%", Icons.AutoMirrored.Filled.TrendingUp, Color(0xFF7B1FA2))
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(300.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            InfoCard(data = item)
        }
    }
}

@Composable
fun InfoCard(data: InfoCardData){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = data.color)
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = data.title,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(data.value, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
            Text(data.title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        }
    }
}

@Composable
fun RecentActivityList() {
    val activities = listOf(
        ActivityItemData("Vacante cubierta internamente", "Senior Developer - 2 candidatos", "Hace 2 horas", Icons.Default.CheckCircle, Color(0xFF388E3C)),
        ActivityItemData("Nuevos skills registrados", "15 colaboradores actualizaron sus perfiles", "Hoy", Icons.Default.People, Color(0xFF4A86E8)),
        ActivityItemData("Nueva vacante registrada", "UX/UI Designer - Alta prioridad", "Hace 1 día", Icons.Default.Work, Color(0xFFF57C00))
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            activities.forEach { activity ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = activity.icon,
                        contentDescription = null,
                        tint = activity.iconColor,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(activity.iconColor.copy(alpha = 0.1f))
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(activity.title, fontWeight = FontWeight.Bold)
                        Text(activity.subtitle, fontSize = 14.sp)
                        Text(activity.time, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class InfoCardData(val title: String, val value: String, val icon: ImageVector, val color: Color)
data class ActivityItemData(val title: String, val subtitle: String, val time: String, val icon: ImageVector, val iconColor: Color)
