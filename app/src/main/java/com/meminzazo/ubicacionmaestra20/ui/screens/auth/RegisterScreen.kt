package com.meminzazo.ubicacionmaestra20.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meminzazo.ubicacionmaestra20.core.utils.hasInternetConnection

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        var errorMessage: String? by remember { mutableStateOf("") }

        val context = LocalContext.current

        Spacer(
            modifier = Modifier
                .height(64.dp))

        Text(
            text = "Registro",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier
                .height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier
            .height(16.dp)
        )

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier
            .height(16.dp)
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier
                .height(8.dp)
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                Log.d("REGISTER_DEBUG", "Boton presionado")

                errorMessage = validateRegister(
                    email,
                    password,
                    confirmPassword
                )

                if (errorMessage != null) return@Button

                if (!hasInternetConnection(context)){
                    errorMessage = "No hay conexión a internet"
                    return@Button
                }
                else{
                    viewModel.register(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        onSuccess = onRegisterSuccess
                    )
                }
            }
        ) {
            if (viewModel.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp),
                        strokeWidth = 2.dp
                )
            } else{
                Text(text = "Registrar")
            }
        }
    }
}

fun validateRegister(
    email: String,
    password: String,
    confirmPassword: String
): String? {
    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
        return "Por favor, complete todos los campos"
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return "Por favor, ingrese un correo electrónico válido"
    }

    if (password.length < 8){
        return "La contraseña debe tener al menos 8 caracteres"
    }

    if (!password.any { it.isDigit() }) {
        return "La contraseña debe contener al menos un número"
    }

    if (!password.any { it.isUpperCase() }) {
        return "La contraseña debe contener al menos una letra mayúscula"
    }
    if (!password.any { it.isLowerCase() }) {
        return "La contraseña debe contener al menos una letra minúscula"
    }

    if (!password.any { !it.isLetterOrDigit() }) {
        return "La contraseña debe contener al menos un carácter especial"
    }

    if (password.any { it.isWhitespace() }) {
        return "La contraseña no puede contener espacios en blanco"
    }

    if (password != confirmPassword){
        return "Las contraseñas no coinciden"
    }

    return null //Todo bien
}

@Preview(
    //uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}
