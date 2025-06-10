package es.gaspardev.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.dtos.RegisterTrainerData
import es.gaspardev.core.domain.usecases.create.RegisterNewTrainer
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import es.gaspardev.utils.validateEmail
import fit_me.composeapp.generated.resources.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegistScreen() {
    val controller = LocalRouter.current
    val scrollState = rememberScrollState()
    
    // Estados del formulario
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var yearsOfExperience by remember { mutableStateOf("") }
    
    // Estados de validación y UI
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }
    
    // Focus requesters
    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val focusRequesterConfirmPassword = remember { FocusRequester() }
    val focusRequesterSpecialization = remember { FocusRequester() }
    val focusRequesterExperience = remember { FocusRequester() }
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
        
        if (specialization.isBlank()) return "La especialización es obligatoria"
        if (specialization.length < 3) return "La especialización debe tener al menos 3 caracteres"
        
        if (yearsOfExperience.isBlank()) return "Los años de experiencia son obligatorios"
        val years = yearsOfExperience.toIntOrNull()
        if (years == null) return "Los años de experiencia deben ser un número válido"
        if (years < 0) return "Los años de experiencia no pueden ser negativos"
        if (years > 50) return "Los años de experiencia no pueden exceder 50"
        
        return null
    }
    
    fun registrarEntrenador() {
        val validationError = validateForm()
        if (validationError != null) {
            error = validationError
            return
        }
        
        error = ""
        isLoading = true
        
        CoroutineScope(Dispatchers.IO).launch {
            val registerData = RegisterTrainerData(
                userName = fullName,
                password = password,
                email = email,
                specialization = specialization,
                yearsOfExperience = yearsOfExperience.toInt()
            )
            
            RegisterNewTrainer(TrainerRepositoryImp()).run(registerData).fold(
                onSuccess = { _ ->
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        success = true
                    }
                },
                onFailure = { throwable ->
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        error = throwable.message ?: "Error al registrar el entrenador"
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
                .background(MaterialTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(450.dp)
                    .padding(24.dp),
                backgroundColor = MaterialTheme.colors.surface
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
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Tu cuenta ha sido creada exitosamente. Ya puedes iniciar sesión con tus credenciales.",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = { controller.navigateTo(Routes.Login) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ir al Login")
                    }
                }
            }
        }
    } else {
        // Formulario de registro
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
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
                        onClick = { controller.navigateTo(Routes.Login) },
                        enabled = !isLoading
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Logo y título
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(Res.drawable.Weights),
                        stringResource(Res.string.fit_me_icon_description),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    stringResource(Res.string.app_title_display),
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    "Registro de Entrenador",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Formulario
                Card(
                    modifier = Modifier
                        .width(500.dp)
                        .padding(horizontal = 24.dp),
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(32.dp)) {
                        Text(
                            "Crear nueva cuenta",
                            style = MaterialTheme.typography.h2,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            "Completa todos los campos para crear tu cuenta de entrenador",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Mensaje de error
                        if (error.isNotEmpty()) {
                            Text(
                                error,
                                color = Color.Red,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .background(Color(0x20FF4444), RoundedCornerShape(4.dp))
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        // Nombre completo
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Nombre completo *") },
                            placeholder = { Text("Ej: Juan Pérez García") },
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
                                onNext = { focusRequesterSpecialization.requestFocus() }
                            ),
                            enabled = !isLoading,
                            isError = confirmPassword.isNotBlank() && password != confirmPassword
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Especialización
                        OutlinedTextField(
                            value = specialization,
                            onValueChange = { specialization = it },
                            label = { Text("Especialización *") },
                            placeholder = { Text("Ej: Entrenamiento funcional, Musculación, etc.") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterSpecialization),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusRequesterExperience.requestFocus() }
                            ),
                            enabled = !isLoading,
                            isError = specialization.isNotBlank() && specialization.length < 3
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Años de experiencia
                        OutlinedTextField(
                            value = yearsOfExperience,
                            onValueChange = { 
                                // Solo permitir números
                                if (it.all { char -> char.isDigit() } && it.length <= 2) {
                                    yearsOfExperience = it
                                }
                            },
                            label = { Text("Años de experiencia *") },
                            placeholder = { Text("Ej: 5") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterExperience),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { 
                                    focusManager.clearFocus()
                                    registrarEntrenador()
                                }
                            ),
                            enabled = !isLoading,
                            isError = yearsOfExperience.isNotBlank() && 
                                      (yearsOfExperience.toIntOrNull() == null || 
                                       yearsOfExperience.toIntOrNull()!! < 0 || 
                                       yearsOfExperience.toIntOrNull()!! > 50)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Texto informativo sobre la contraseña
                        Text(
                            text = "La contraseña debe contener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Botón de registro
                        Button(
                            onClick = { registrarEntrenador() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colors.onPrimary
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
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            TextButton(
                                onClick = { controller.navigateTo(Routes.Login) },
                                enabled = !isLoading
                            ) {
                                Text(
                                    text = "Iniciar sesión",
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.primary
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
