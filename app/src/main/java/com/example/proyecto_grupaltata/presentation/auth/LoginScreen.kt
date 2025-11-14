package com.example.proyecto_grupaltata.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.R
import com.example.proyecto_grupaltata.ui.theme.BlueTCS
import com.example.proyecto_grupaltata.ui.theme.OrangeTcs


@Composable
// Login Screen :3
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf(value = "") }
    var password by remember { mutableStateOf(value = "") }

    Column(
        modifier = Modifier.padding(16.dp),
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        // Logo:
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de Tata Consultancy Services",
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Titulo general del logeo
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = OrangeTcs,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Titulo del Login
        Text(text = "Correo:", style = MaterialTheme.typography.titleMedium, color = BlueTCS)

        // Input del email c:
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            placeholder = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(), // El espacio completo para la barra
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Icono de usuario"
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))


        // Titulo contraseña:
        Text(text = "Contraseña:", style = MaterialTheme.typography.titleMedium, color = BlueTCS)

        // Input contraseña:
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Icono de constraseña"
                )
            },
            visualTransformation = PasswordVisualTransformation()
        )

        var rememberMe by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text("Recuérdame", fontWeight = FontWeight.Bold)
            }

            TextButton(onClick = { /* TODO: CONECTAR FIREBASE Y RECUPERAR CONTRASEÑA */ }) {
                Text("¿Olvidaste tu Contraseña?", color = BlueTCS)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("vacancies") }) {
            Text("Go to Vacancies Screen (Temp)")
        }
    }
}