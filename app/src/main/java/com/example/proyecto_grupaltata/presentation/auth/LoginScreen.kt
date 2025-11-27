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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_grupaltata.R
import com.example.proyecto_grupaltata.presentation.main.MainViewModel
import com.example.proyecto_grupaltata.presentation.navigation.AppScreens

@Composable
fun LoginScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val loginState = loginViewModel.loginState.collectAsState().value

    // Observador para manejar el resultado del login y otros eventos
    LaunchedEffect(loginState) {
        // Manejar éxito de login
        loginState.loginSuccess?.let { userId ->
            mainViewModel.fetchCollaborator(userId)
            Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            navController.navigate(AppScreens.HomeScreen.route) {
                popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
            }
        }
        // Manejar errores
        loginState.error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            loginViewModel.clearError()
        }
        // Manejar mensajes (como el de recuperación de contraseña)
        loginState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            loginViewModel.clearToastMessage()
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

            Text(text = "Inicio de Sesión", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = loginViewModel.email,
                onValueChange = { loginViewModel.onValueChange(it, "email") },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = loginViewModel.password,
                onValueChange = { loginViewModel.onValueChange(it, "password") },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // **NUEVO: Botón para recuperar contraseña**
            TextButton(onClick = { loginViewModel.resetPassword() }) {
                Text("¿Olvidaste tu contraseña?")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { loginViewModel.loginUser() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Ingresar")
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { navController.navigate(AppScreens.RegisterScreen.route) }) {
                Text("¿No tienes una cuenta? Regístrate")
            }
        }

        if (loginState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
