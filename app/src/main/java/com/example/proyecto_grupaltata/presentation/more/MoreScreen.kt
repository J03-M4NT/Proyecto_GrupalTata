package com.example.proyecto_grupaltata.presentation.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.R
import com.example.proyecto_grupaltata.model.Usuario
import com.example.proyecto_grupaltata.presentation.main.MainViewModel
import com.example.proyecto_grupaltata.presentation.navigation.AppScreens
import com.example.proyecto_grupaltata.ui.theme.BlueTCS
import com.example.proyecto_grupaltata.ui.theme.OrangeTcs
import com.google.firebase.auth.FirebaseAuth

// Función para obtener iniciales, necesaria para la tarjeta de perfil
fun String.getInitials(): String {
    return this.split(' ')
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first() }
        .joinToString("")
        .uppercase()
}

@Composable
fun MoreScreen(navController: NavController, mainViewModel: MainViewModel) {
    // Recoger el usuario actual del MainViewModel
    val usuario by mainViewModel.usuario.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "TCS Logo",
            modifier = Modifier.height(24.dp).align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // **TARJETA DE PERFIL DINÁMICA**
        usuario?.let { UserInfoCard(it) }

        Spacer(modifier = Modifier.height(32.dp))

        // **SECCIÓN DE CUENTA (VISIBLE PARA TODOS)**
        SectionHeader("Cuenta")
        Spacer(modifier = Modifier.height(16.dp))
        ActionCard(
            icon = Icons.Default.Person,
            title = "Mi Perfil",
            subtitle = "Ver y editar información personal",
            onClick = { navController.navigate(AppScreens.ProfileScreen.route) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        ActionCard(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Cerrar Sesión",
            subtitle = "Salir de forma segura",
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(AppScreens.LoginScreen.route) { popUpTo(0) }
            }
        )

        // **SECCIÓN DE ADMIN (VISIBLE SOLO PARA ROLES ADMIN)**
        val adminRoles = setOf("HR", "Admin", "Administrador")
        if (usuario?.rol in adminRoles) {
            Spacer(modifier = Modifier.height(32.dp))
            SectionHeader("Herramientas HR/Admin")
            Spacer(modifier = Modifier.height(16.dp))
            ActionCard(
                icon = Icons.Default.Description,
                title = "Reportes de Actividad",
                subtitle = "Exportar y descargar reportes",
                onClick = { navController.navigate(AppScreens.ReportActivityScreen.route) }
            )
        }
    }
}

@Composable
private fun UserInfoCard(usuario: Usuario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BlueTCS)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = usuario.nombre.getInitials(),
                    color = BlueTCS,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(usuario.nombre, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(usuario.rol, color = OrangeTcs, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 16.sp)
}

@Composable
private fun ActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, tint = OrangeTcs, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, fontSize = 13.sp, color = Color.Gray)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = Color.LightGray)
        }
    }
}
