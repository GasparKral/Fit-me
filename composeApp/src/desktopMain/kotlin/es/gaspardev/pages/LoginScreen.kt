package es.gaspardev.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.dtos.LoginCredentials
import es.gaspardev.core.domain.usecases.read.GetConversations
import es.gaspardev.core.domain.usecases.read.LogInUser
import es.gaspardev.core.infrastructure.memo.CacheManager
import es.gaspardev.core.infrastructure.memo.CacheRef
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import es.gaspardev.states.ConversationsState
import es.gaspardev.states.LoggedTrainer
import es.gaspardev.utils.encrypt
import fit_me.composeapp.generated.resources.*
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.Weights
import fit_me.composeapp.generated.resources.fit_me_icon_description
import fit_me.composeapp.generated.resources.login_error_generic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen() {

    val genericError = stringResource(Res.string.login_error_generic)

    val controller = LocalRouter.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect("loadCache", block = {
        if (CacheManager.hasKey(CacheRef.RememberUserName)) {
            username = CacheManager.getValue(CacheRef.RememberUserName).toString()
            rememberMe = true
        }
    })

    val focusRequesterUsername = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    fun iniciarSesion() {
        error = ""
        isLoading = true

        CoroutineScope(Dispatchers.IO).launch {
            LogInUser(TrainerRepositoryImp()).run(LoginCredentials(username, encrypt(password)))
                .fold(
                    onSuccess = { result ->
                        withContext(Dispatchers.Main) {
                            isLoading = false
                            LoggedTrainer.login(result.first, result.second)
                            if (rememberMe) {
                                CacheManager.saveValue(CacheRef.RememberUserName, result.first.user.fullname)
                            }
                            ConversationsState.loadConversations(result.third)
                            controller.navigateTo(Routes.Dashboard)
                        }
                    },
                    onFailure = { throwable ->
                        withContext(Dispatchers.Main) {
                            isLoading = false
                            error = throwable.message ?: genericError
                            println("Login failed: ${throwable.message}")
                            throw throwable
                        }
                    }
                )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(Res.string.app_title_display),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.primary
            )
            Text(
                stringResource(Res.string.inicio_de_sesion_portal),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .width(450.dp)
                    .padding(24.dp),
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(Res.string.inicio_de_sesion), style = MaterialTheme.typography.subtitle2)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (error.isNotEmpty()) {
                        Text(
                            error,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.background(Color(0x50FF4000), RoundedCornerShape(4.dp)).padding(4.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text(stringResource(Res.string.inicio_de_sesion_usuario)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterUsername)
                            .onPreviewKeyEvent {
                                if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                                    focusRequesterPassword.requestFocus()
                                    true
                                } else {
                                    false
                                }
                            },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(Res.string.inicio_de_sesion_contraseña)) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterPassword)
                            .onPreviewKeyEvent {
                                if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                                    focusManager.clearFocus()
                                    true
                                } else if (it.key == Key.Enter && it.type == KeyEventType.KeyDown) {
                                    focusManager.clearFocus()
                                    iniciarSesion()
                                    true
                                } else {
                                    false
                                }
                            },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            iniciarSesion()
                        })
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            stringResource(Res.string.inicio_de_sesion_recuerdame),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { iniciarSesion() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) stringResource(Res.string.signing_in) else stringResource(Res.string.sign_in))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿No tienes cuenta?",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(
                            onClick = { controller.navigateTo(Routes.Regist) },
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Registrarse",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }

                }
            }
        }
    }


}

