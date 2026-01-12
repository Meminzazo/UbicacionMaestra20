package com.meminzazo.ubicacionmaestra20.ui.screens.auth.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        val errorMessage = viewModel.errorMessage


        Spacer(
            modifier = Modifier
                .height(64.dp)
        )

        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier
            .height(32.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Correo") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier
            .height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Contraseña") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier
            .height(16.dp)
        )
        errorMessage?.let{
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
                Log.d("LOGIN_DEBUG", "Boton presionado")
                viewModel.login(
                    email = email,
                    password = password,
                    onSuccess = onLoginSuccess
                )
            },
            enabled = !viewModel.isLoading
        ) {
            Text(text = "Iniciar Sesión")
        }

        Spacer(modifier = Modifier
            .height(8.dp)
        )

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp),
            onClick = {
                onForgotPassword()
            }
        ){
            Text(text = "Olvidé mi contraseña",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

}