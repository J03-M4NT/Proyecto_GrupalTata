package com.example.proyecto_grupaltata.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.R
import com.example.proyecto_grupaltata.presentation.navigation.AppScreens

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    val registerState = registerViewModel.registerState.collectAsState()

    // Navegar a la pantalla de Login si el registro fue exitoso
    LaunchedEffect(registerState.value) {
        if (registerState.value.isSuccess) {
            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
        registerState.value.error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo")
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Registro de Usuario", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = registerViewModel.username,
                onValueChange = { registerViewModel.onValueChange(it, "username") },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = registerViewModel.email,
                onValueChange = { registerViewModel.onValueChange(it, "email") },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = registerViewModel.password,
                onValueChange = { registerViewModel.onValueChange(it, "password") },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { registerViewModel.registerUser() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Registrarme")
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { navController.popBackStack() }) {
                Text("¿Ya tienes una cuenta? Inicia Sesión")
            }
        }

        if (registerState.value.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}