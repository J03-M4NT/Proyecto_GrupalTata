package com.example.proyecto_grupaltata.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
fun LoginScreen(navController: NavController){

    // TODO: Aaaaah var and functions owo
    var email by remember { mutableStateOf(value = "") }
    var password by remember { mutableStateOf(value = "") }

    /* -------------------------------------------- */
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp),
    ) {

        //Spacer(modifier = Modifier.weight(24.dp))
        Spacer(modifier = Modifier.height(24.dp))

        // Logo:
        Image(
            // painterResource carga el logo desde drawable
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de Tata Consultancy Services",
            modifier = Modifier
                .fillMaxWidth(0.7f)     // 60% del ancho
                //.size(300.dp)
                .padding(bottom = 32.dp)        // Espacio bajo del logo
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

        // ---------------------------

        // Titulo del Login
        Text( text = "Correo:", style = MaterialTheme.typography.titleMedium, color = BlueTCS)

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
        Text( text = "Contraseña:", style = MaterialTheme.typography.titleMedium, color = BlueTCS )

        // Input contraseña:
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },     // Label muestra eso escrito, dentro de la caja
            placeholder = { Text("Contraseña") },   // Muestra encima como subsección, depende su uso
            modifier = Modifier.fillMaxWidth(),  // Espacio completo para la barra
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Icono de constraseña"
                )
            },
            visualTransformation = PasswordVisualTransformation()   // Solo se veran puntos al escribir
        )

        var rememberMe by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween    // Empuja a los extremos
        ) {
            // Seccion recuerdame:
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text("Recuérdame", fontWeight = FontWeight.Bold)
            }

            // Sección "Olvidaste tu contraseña"
            TextButton(onClick = { /* TODO: CONECTAR FIREBASE Y RECUPERAR CONTRASEÑA */ }) {
                Text("¿Olvidaste tu Contraseña?", color = BlueTCS)
            }


        }



    }



}