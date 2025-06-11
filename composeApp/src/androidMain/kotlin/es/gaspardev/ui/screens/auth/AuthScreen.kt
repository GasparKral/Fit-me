package es.gaspardev.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.gaspardev.utils.DeepLinkManager

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    deepLinkData: DeepLinkManager.DeepLinkData? = null,
    viewModel: AuthViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf(AuthScreenType.LOGIN) }
    var registrationToken by remember { mutableStateOf<String?>(null) }
    var showTokenInput by remember { mutableStateOf(false) }

    // Procesar deep link cuando llegue
    LaunchedEffect(deepLinkData) {
        deepLinkData?.let { data ->
            when (data.action) {
                DeepLinkManager.DeepLinkAction.REGISTER -> {
                    registrationToken = data.token
                    currentScreen = AuthScreenType.REGISTER
                }

                DeepLinkManager.DeepLinkAction.LOGIN -> {
                    currentScreen = AuthScreenType.LOGIN
                }

                else -> {
                    // Manejar acción desconocida
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header de la app
        AppHeader()

        Spacer(modifier = Modifier.height(32.dp))

        // Mostrar información del deep link si está presente
        deepLinkData?.let { data ->
            DeepLinkInfo(data = data)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Contenido principal basado en la pantalla actual
        when (currentScreen) {
            AuthScreenType.LOGIN -> {
                LoginForm(
                    onNavigateToRegister = {
                        showTokenInput = true
                    },
                    onShowTokenInput = { showTokenInput = true },
                    viewModel = viewModel
                )
            }

            AuthScreenType.REGISTER -> {
                RegisterForm(
                    onNavigateToLogin = {
                        currentScreen = AuthScreenType.LOGIN
                        showTokenInput = false
                    },
                    registrationToken = registrationToken,
                    viewModel = viewModel
                )
            }
        }

        // Modal para introducir token manualmente
        if (showTokenInput) {
            TokenInputDialog(
                onTokenConfirmed = { token ->
                    registrationToken = token
                    currentScreen = AuthScreenType.REGISTER
                    showTokenInput = false
                },
                onDismiss = { showTokenInput = false },
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun AppHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FitMe",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Tu entrenador personal",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DeepLinkInfo(data: DeepLinkManager.DeepLinkData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            when (data.action) {
                DeepLinkManager.DeepLinkAction.REGISTER -> {
                    Text(
                        text = "🎯 Invitación de Registro",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Has sido invitado a registrarte como atleta. Completa el formulario para crear tu cuenta.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                DeepLinkManager.DeepLinkAction.LOGIN -> {
                    Text(
                        text = "🔑 Enlace de Acceso",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Enlace de acceso rápido detectado.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    Text(
                        text = "ℹ️ Enlace Detectado",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (data.token != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Token: ${data.token.take(8)}...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun LoginForm(
    onNavigateToRegister: () -> Unit,
    onShowTokenInput: () -> Unit,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Para entrenadores",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email o Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.login(email, password)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

            Divider(
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Sección para atletas
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¿Eres un atleta?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onShowTokenInput,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tengo un código de registro")
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "O escanea el QR que te proporcionó tu entrenador",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun RegisterForm(
    onNavigateToLogin: () -> Unit,
    registrationToken: String?,
    viewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registro de Atleta",
                style = MaterialTheme.typography.headlineSmall
            )

            // Mostrar información del token si está presente
            registrationToken?.let { token ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "✅ Token de Registro Válido",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "Tu entrenador te ha invitado a registrarte.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.registerAthlete(
                        name = name,
                        email = email,
                        age = age.toIntOrNull() ?: 0,
                        password = password,
                        registrationToken = registrationToken
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = registrationToken != null &&
                        name.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        password == confirmPassword
            ) {
                Text("Registrarse")
            }

            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}

@Composable
private fun TokenInputDialog(
    onTokenConfirmed: (String) -> Unit,
    onDismiss: () -> Unit,
    viewModel: AuthViewModel
) {
    var token by remember { mutableStateOf("") }
    var isValidating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Código de Registro")
        },
        text = {
            Column {
                Text(
                    text = "Introduce el código que te proporcionó tu entrenador:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = token,
                    onValueChange = {
                        token = it.uppercase().filter { char -> char.isLetterOrDigit() }
                        errorMessage = null
                    },
                    label = { Text("Código de registro") },
                    placeholder = { Text("Ej: ABC123DEF456") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = errorMessage != null
                )

                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (token.isBlank()) {
                        errorMessage = "El código no puede estar vacío"
                        return@Button
                    }

                    if (token.length < 10) {
                        errorMessage = "El código debe tener al menos 10 caracteres"
                        return@Button
                    }

                    isValidating = true
                    // Aquí podrías agregar validación del token con el servidor
                    // Por ahora, aceptamos cualquier token válido
                    onTokenConfirmed(token)
                },
                enabled = !isValidating && token.isNotBlank()
            ) {
                if (isValidating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Continuar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

enum class AuthScreenType {
    LOGIN,
    REGISTER
}