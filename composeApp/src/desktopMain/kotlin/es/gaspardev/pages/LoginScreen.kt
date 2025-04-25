package es.gaspardev.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.controllers.LoggedUser
import es.gaspardev.core.Routing.Route
import es.gaspardev.core.Routing.RouterController
import es.gaspardev.core.domain.DAOs.LoginUserInfo
import es.gaspardev.core.infrastructure.memo.CacheManager
import es.gaspardev.core.infrastructure.memo.CacheRef
import es.gaspardev.core.infrastructure.repositories.UserRepositoryImp
import es.gaspardev.utils.encrypt
import fit_me.composeapp.generated.resources.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LoginScreen(controller: RouterController) {

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


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
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
                    "Fit-Me Icon",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Fit-me",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
            Text(
                stringResource(Res.string.inicio_de_sesion_portal),
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .width(450.dp)
                    .padding(16.dp)
                    .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(12.dp)),
                elevation = 0.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(Res.string.inicio_de_sesion), fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(Res.string.inicio_de_sesion_contraseña)) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(Res.string.inicio_de_sesion_recuerdame))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            error = ""
                            isLoading = true

                            // Llamar a la función suspend dentro de un CoroutineScope
                            CoroutineScope(Dispatchers.IO).launch {
                                UserRepositoryImp().logIn(LoginUserInfo(username, encrypt(password)))
                                    .fold(
                                        onSuccess = { result ->
                                            // Manejar el caso de éxito
                                            withContext(Dispatchers.Main) {
                                                isLoading = false
                                                LoggedUser.user = result[0]
                                                if (rememberMe) {
                                                    CacheManager.saveValue(CacheRef.RememberUserName, result[0].name)
                                                }

                                                controller.navigateTo(Route.Dashboard)
                                            }
                                        },
                                        onFailure = { throwable ->
                                            // Manejar el caso de error
                                            withContext(Dispatchers.Main) {
                                                isLoading = false
                                                error = throwable.message ?: "An error occurred"
                                                println("Login failed: ${throwable.message}")
                                            }
                                        }
                                    )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "Signing in..." else "Sign in")
                    }

                }
            }
        }
    }

}

