package com.example.proyecto_grupaltata.presentation.more

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_grupaltata.R
import com.example.proyecto_grupaltata.presentation.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "TCS Logo",
                        modifier = Modifier.height(20.dp)
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("María González", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("HR Manager", fontSize = 12.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0055D4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("MG", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = Color(0xFFF7F7F7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Más Opciones", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFD7E14))
            Text("Configuración y herramientas adicionales", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            UserInfoCard()

            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(icon = Icons.Default.AdminPanelSettings, title = "Herramientas HR/Admin")
            Spacer(modifier = Modifier.height(16.dp))
            ActionCard(
                icon = Icons.Default.Description,
                title = "Reportes de Actividad",
                subtitle = "Exportar y descargar reportes desde Firebase",
                tag = "HR",
                iconColor = Color(0xFF6f42c1),
                tagColor = Color(0xFF6f42c1),
                cardColor = Color.White,
                onClick = { navController.navigate(AppScreens.ReportActivityScreen.route) } // Navegación añadida
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(title = "Cuenta")
            Spacer(modifier = Modifier.height(16.dp))
            ActionCard(
                icon = Icons.Default.Person,
                title = "Mi Perfil",
                subtitle = "Ver y editar información personal",
                iconColor = Color(0xFF0d6efd),
                cardColor = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            ActionCard(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = "Cerrar Sesión",
                subtitle = "Salir de forma segura",
                iconColor = Color.Red,
                cardColor = Color(0xFFFFF0F0),
                onClick = { /* TODO: Handle Logout */ }
            )

            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("TCS Talent Management System", fontSize = 12.sp, color = Color.Gray)
                Text("Versión 1.0.0 • © 2025 TCS", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun UserInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0055D4))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text("MG", color = Color(0xFF0055D4), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("María González", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.height(28.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                ) {
                    Text("HR Manager", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector? = null, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        icon?.let {
            Icon(imageVector = it, contentDescription = title, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
        Text(title, fontWeight = FontWeight.Medium, color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
private fun ActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    cardColor: Color,
    tag: String? = null,
    tagColor: Color? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier
                    .size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    tag?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(tagColor ?: Color.Transparent)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = it, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Text(subtitle, fontSize = 13.sp, color = Color.Gray)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun MoreScreenPreview() {
    MaterialTheme {
        MoreScreen(rememberNavController())
    }
}
