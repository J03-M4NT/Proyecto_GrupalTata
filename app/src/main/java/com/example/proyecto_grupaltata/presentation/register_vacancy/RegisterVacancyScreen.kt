package com.example.proyecto_grupaltata.presentation.register_vacancy

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.proyecto_grupaltata.domain.model.Vacancy
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVacancyDialog(
    onDismissRequest: () -> Unit,
    onVacancyRegistered: (Vacancy) -> Unit
) {
    var profileName by remember { mutableStateOf("") }
    var projectAccount by remember { mutableStateOf("") }
    var desiredLevel by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var urgency by remember { mutableStateOf("") }
    val selectedSkills = remember { mutableStateListOf<String>() }
    var showSkillsDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    var isLevelExpanded by remember { mutableStateOf(false) }
    val levelOptions = listOf("Nivel 1", "Nivel 2", "Nivel 3", "Nivel 4", "Nivel 5")

    var isUrgencyExpanded by remember { mutableStateOf(false) }
    val urgencyOptions = listOf("Baja", "Media", "Alta")

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            startDate = sdf.format(GregorianCalendar(year, month, dayOfMonth).time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // ... (Header and other fields remain the same)
                 Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nueva Vacante", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismissRequest, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text("Completa la información del perfil requerido", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = profileName,
                    onValueChange = { profileName = it },
                    label = { Text("Nombre del Perfil *") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) }
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = projectAccount,
                    onValueChange = { projectAccount = it },
                    label = { Text("Cuenta/Proyecto *") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) }
                )
                Spacer(Modifier.height(16.dp))

                Box(modifier = Modifier.clickable { showSkillsDialog = true }) {
                    OutlinedTextField(
                        value = selectedSkills.joinToString(", ").ifEmpty { "" },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Skills Requeridos *") },
                        placeholder = { Text("Seleccionar skills") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { Icon(Icons.Default.Add, contentDescription = "Añadir Skill") },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                Spacer(Modifier.height(16.dp))

                 ExposedDropdownMenuBox(expanded = isLevelExpanded, onExpandedChange = { isLevelExpanded = !isLevelExpanded }) {
                    OutlinedTextField(
                        value = desiredLevel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nivel Deseado *") },
                        placeholder = { Text("Seleccionar nivel") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLevelExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = isLevelExpanded, onDismissRequest = { isLevelExpanded = false }) {
                        levelOptions.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = { desiredLevel = option; isLevelExpanded = false })
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                Box(modifier = Modifier.clickable { datePickerDialog.show() }) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = {},
                        label = { Text("Fecha de Inicio *") },
                        placeholder = { Text("dd/mm/aaaa") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Abrir calendario") },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                Spacer(Modifier.height(16.dp))

                ExposedDropdownMenuBox(expanded = isUrgencyExpanded, onExpandedChange = { isUrgencyExpanded = !isUrgencyExpanded }) {
                    OutlinedTextField(
                        value = urgency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Urgencia *") },
                        placeholder = { Text("Seleccionar urgencia") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isUrgencyExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = isUrgencyExpanded, onDismissRequest = { isUrgencyExpanded = false }) {
                        urgencyOptions.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = { urgency = option; isUrgencyExpanded = false })
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))


                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismissRequest) { Text("Cancelar") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val isFormValid = profileName.isNotBlank() &&
                                        projectAccount.isNotBlank() &&
                                        desiredLevel.isNotBlank() &&
                                        startDate.isNotBlank() &&
                                        urgency.isNotBlank() &&
                                        selectedSkills.isNotEmpty()

                        if (isFormValid) {
                            val newVacancy = Vacancy(
                                profileName = profileName,
                                projectAccount = projectAccount,
                                requiredSkills = selectedSkills.toList(),
                                desiredLevel = desiredLevel,
                                startDate = startDate,
                                urgency = urgency
                            )
                            onVacancyRegistered(newVacancy)
                        } else {
                            Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    }) { Text("Registrar Vacante") }
                }
            }
        }
    }

    if (showSkillsDialog) {
        SkillsSelectionDialog(
            initialSelection = selectedSkills,
            onDismissRequest = {
                selectedSkills.clear()
                selectedSkills.addAll(it)
                showSkillsDialog = false
            }
        )
    }
}

@Composable
private fun SkillsSelectionDialog(
    initialSelection: List<String>,
    onDismissRequest: (List<String>) -> Unit
) {
    val allSkills = listOf("React", "Angular", "Vue.js", "Node.js", "Python", "Java", "TypeScript", "JavaScript", "MongoDB", "AWS", "Docker", "GIT")
    val selectedSkills = remember { mutableStateListOf<String>().also { it.addAll(initialSelection) } }

    Dialog(onDismissRequest = { onDismissRequest(initialSelection) }) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Seleccionar Skills", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    items(allSkills) { skill ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    if (selectedSkills.contains(skill)) {
                                        selectedSkills.remove(skill)
                                    } else {
                                        selectedSkills.add(skill)
                                    }
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedSkills.contains(skill),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        if (!selectedSkills.contains(skill)) selectedSkills.add(skill)
                                    } else {
                                        selectedSkills.remove(skill)
                                    }
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(skill)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismissRequest(initialSelection) }) { Text("Cancelar") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onDismissRequest(selectedSkills.toList()) }) { Text("Confirmar") }
                }
            }
        }
    }
}