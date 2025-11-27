package com.example.proyecto_grupaltata.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.model.ActivityItem
import com.example.proyecto_grupaltata.model.DashboardStats
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel() // 1. Se conecta al ViewModel
) {
    // 2. Se suscribe a los datos en tiempo real
    val dashboardStats by homeViewModel.dashboardStats.collectAsState()
    val recentActivity by homeViewModel.recentActivity.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("¡Bienvenido!", style = MaterialTheme.typography.headlineMedium, color = Color.Gray)
        Text(
            "Sistema de Gestión de Talento Interno",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        // 3. Pasa los datos VIVOS al DashboardGrid
        DashboardGrid(stats = dashboardStats)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Actividad Reciente",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        // 4. Pasa la actividad VIVA a la lista
        RecentActivityList(activities = recentActivity)
    }
}


@Composable
fun DashboardGrid(stats: DashboardStats) { // Ahora recibe el estado del dashboard
    val items = listOf(
        InfoCardData("Colaboradores", stats.collaboratorCount.toString(), Icons.Default.People, Color(0xFF4A86E8)),
        InfoCardData("Vacantes Abiertas", stats.openVacanciesCount.toString(), Icons.Default.Work, Color(0xFFF57C00)),
        InfoCardData("Skills Mapeados", "245", Icons.Default.Checklist, Color(0xFF388E3C)), // Dato estático
        InfoCardData("Match Interno", "85%", Icons.AutoMirrored.Filled.TrendingUp, Color(0xFF7B1FA2)) // Dato estático
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
fun RecentActivityList(activities: List<ActivityItem>) { // Ahora recibe la lista de actividades del ViewModel
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (activities.isEmpty()) {
                Text(
                    text = "No hay actividad reciente.",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                )
            } else {
                activities.forEachIndexed { index, activity ->
                    val (icon, color) = getActivityIconAndColor(activity.type)
                    val formattedTime = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(activity.timestamp.toDate())

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color.copy(alpha = 0.1f))
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(activity.title, fontWeight = FontWeight.Bold)
                            Text(activity.subtitle, fontSize = 14.sp)
                            Text(formattedTime, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    if (index < activities.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

// Función auxiliar para obtener el icono y color según el tipo de actividad
@Composable
private fun getActivityIconAndColor(type: String): Pair<ImageVector, Color> {
    return when (type) {
        "NEW_VACANCY" -> Icons.Default.Work to Color(0xFFF57C00)
        "VACANCY_FILLED" -> Icons.Default.CheckCircle to Color(0xFF388E3C)
        "NEW_COLLABORATOR" -> Icons.Default.People to Color(0xFF4A86E8)
        else -> Icons.Default.Checklist to Color.Gray
    }
}

data class InfoCardData(val title: String, val value: String, val icon: ImageVector, val color: Color)
// La data class ActivityItemData ya no es necesaria, usamos ActivityItem del modelo.
