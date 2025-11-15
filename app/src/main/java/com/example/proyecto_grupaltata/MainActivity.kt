package com.example.proyecto_grupaltata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyecto_grupaltata.presentation.navigation.AppNavGraph
import com.example.proyecto_grupaltata.ui.theme.Proyecto_GrupalTataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_GrupalTataTheme {
                AppNavGraph()
            }
        }
    }
}