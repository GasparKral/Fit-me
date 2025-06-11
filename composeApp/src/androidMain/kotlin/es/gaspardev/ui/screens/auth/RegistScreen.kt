package es.gaspardev.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import es.gaspardev.core.domain.dtos.RegisterAthleteData
import es.gaspardev.core.domain.usecases.create.user.ResgisterNewAthlete
import es.gaspardev.core.infrastructure.repositories.AthletetRepositoryImp
import es.gaspardev.enums.Sex
import es.gaspardev.ui.screens.destinations.LoginScreenDestination
import es.gaspardev.utils.validateEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Destination
@Composable
fun AthleteRegistScreen(
    navigator: DestinationsNavigator
) {
    val scrollState = rememberScrollState()

    // Estados del formulario
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf(Sex.MALE) }
    var trainerId by remember { mutableStateOf("") }

    // Estados de validación y UI
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }
    var sexExpanded by remember { mutableStateOf(false) }

    // Focus requesters
    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val focusRequesterConfirmPassword = remember { FocusRequester() }
    val focusRequesterAge = remember { FocusRequester() }
    val focusRequesterTrainerId = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Validaciones
    fun validateForm(): String? {
        if (fullName.isBlank()) return "El nombre completo es obligatorio"
        if (fullName.length < 2) return "El nombre debe tener al menos 2 caracteres"

        if (email.isBlank()) return "El email es obligatorio"
        if (!validateEmail(email)) return "El formato del email no es válido"

        if (password.isBlank()) return "La contraseña es obligatoria"
        if (password.length < 8) return "La contraseña debe tener al menos 8 caracteres"
        if (!password.any { it.isUpperCase() }) return "La contraseña debe contener al menos una letra mayúscula"
        if (!password.any { it.isLowerCase() }) return "La contraseña debe contener al menos una letra minúscula"
        if (!password.any { it.isDigit() }) return "La contraseña debe contener al menos un número"
        if (!password.any { !it.isLetterOrDigit() }) return "La contraseña debe contener al menos un carácter especial"

        if (confirmPassword.isBlank()) return "La confirmación de contraseña es obligatoria"
        if (password != confirmPassword) return "Las contraseñas no coinciden"

        if (age.isBlank()) return "La edad es obligatoria"
        val ageInt = age.toIntOrNull()
        if (ageInt == null) return "La edad debe ser un número válido"
        if (ageInt < 13) return "Debes tener al menos 13 años para registrarte"
        if (ageInt > 120) return "La edad ingresada no es válida"

        if (trainerId.isBlank()) return "El ID del entrenador es obligatorio"
        val trainerIdInt = trainerId.toIntOrNull()
        if (trainerIdInt == null) return "El ID del entrenador debe ser un número válido"

        return null
    }

    fun registrarAtleta() {
        val validationError = validateForm()
        if (validationError != null) {
            error = validationError
            return
        }

        error = ""
        isLoading = true

        CoroutineScope(Dispatchers.IO).launch {
            val registerData = RegisterAthleteData(
                userName = fullName,
                password = password,
                email = email,
                sex = sex == Sex.MALE, // true para hombre, false para mujer
                age = age.toInt(),
                trainerId = trainerId.toInt()
            )

            ResgisterNewAthlete(AthletetRepositoryImp()).run(registerData).fold(
                onSuccess = { _ ->
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        success = true
                    }
                },
                onFailure = { throwable ->
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        error = throwable.message ?: "Error al registrar el atleta"
                    }
                }
            )
        }
    }

    if (success) {
        // Pantalla de éxito
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(450.dp)
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icono de éxito
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "¡Registro Exitoso!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tu cuenta de atleta ha sido creada exitosamente. Tu entrenador podrá asignarte rutinas y dietas personalizadas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { navigator.navigate(LoginScreenDestination) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar Sesión")
                    }
                }
            }
        }
    } else {
        // Formulario de registro
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header con botón de retroceso
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(
                        onClick = { navigator.navigate(LoginScreenDestination) },
                        enabled = !isLoading
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Logo y título
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💪",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "FitMe",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Registro de Atleta",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Formulario
                Card(
                    modifier = Modifier
                        .width(500.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(32.dp)) {
                        Text(
                            "Crear cuenta de atleta",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Únete para entrenar con profesionales",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Mensaje de error
                        if (error.isNotEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    error,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Nombre completo
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Nombre completo *") },
                            placeholder = { Text("Ej: Ana López García") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterName),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusRequesterEmail.requestFocus() }
                            ),
                            enabled = !isLoading,
                            isError = fullName.isNotBlank() && fullName.length < 2
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email *") },
                            placeholder = { Text("ejemplo@correo.com") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterEmail),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusRequesterPassword.requestFocus() }
                            ),
                            enabled = !isLoading,
                            isError = email.isNotBlank() && !validateEmail(email)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Contraseña
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña *") },
                            placeholder = { Text("Mínimo 8 caracteres") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterPassword),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Password
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusRequesterConfirmPassword.requestFocus() }
                            ),
                            enabled = !isLoading,
                            isError = password.isNotBlank() && password.length < 8
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Confirmar contraseña
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirmar contraseña *") },
                            placeholder = { Text("Repite la contraseña") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterConfirmPassword),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Password
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusRequesterAge.requestFocus() }
                            ),
                            enabled = !isLoading,
                            isError = confirmPassword.isNotBlank() && password != confirmPassword
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Edad y sexo en fila
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Edad
                            OutlinedTextField(
                                value = age,
                                onValueChange = {
                                    if (it.all { char -> char.isDigit() } && it.length <= 3) {
                                        age = it
                                    }
                                },
                                label = { Text("Edad *") },
                                placeholder = { Text("25") },
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequesterAge),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Number
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusRequesterTrainerId.requestFocus() }
                                ),
                                enabled = !isLoading,
                                isError = age.isNotBlank() &&
                                        (age.toIntOrNull() == null ||
                                                age.toIntOrNull()!! < 13 ||
                                                age.toIntOrNull()!! > 120)
                            )

                            // Sexo
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = when (sex) {
                                        Sex.MALE -> "Masculino"
                                        Sex.FEMALE -> "Femenino"
                                    },
                                    onValueChange = { },
                                    label = { Text("Sexo *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    readOnly = true,
                                    enabled = !isLoading,
                                    trailingIcon = {
                                        IconButton(
                                            onClick = { sexExpanded = true }
                                        ) {
                                            Icon(
                                                if (sexExpanded)
                                                    Icons.Default.KeyboardArrowUp
                                                else
                                                    Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Dropdown"
                                            )
                                        }
                                    }
                                )

                                DropdownMenu(
                                    expanded = sexExpanded,
                                    onDismissRequest = { sexExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Masculino") },
                                        onClick = {
                                            sex = Sex.MALE
                                            sexExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Femenino") },
                                        onClick = {
                                            sex = Sex.FEMALE
                                            sexExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ID del entrenador
                        OutlinedTextField(
                            value = trainerId,
                            onValueChange = {
                                if (it.all { char -> char.isDigit() }) {
                                    trainerId = it
                                }
                            },
                            label = { Text("ID del entrenador *") },
                            placeholder = { Text("Proporcionado por tu entrenador") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterTrainerId),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    registrarAtleta()
                                }
                            ),
                            enabled = !isLoading,
                            isError = trainerId.isNotBlank() && trainerId.toIntOrNull() == null
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Texto informativo
                        Text(
                            text = "El ID del entrenador te será proporcionado por tu entrenador personal. Contacta con él para obtenerlo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botón de registro
                        Button(
                            onClick = { registrarAtleta() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(if (isLoading) "Registrando..." else "Crear cuenta")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Link para volver al login
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "¿Ya tienes cuenta?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            TextButton(
                                onClick = { navigator.navigate(LoginScreenDestination) },
                                enabled = !isLoading
                            ) {
                                Text(
                                    text = "Iniciar sesión",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}