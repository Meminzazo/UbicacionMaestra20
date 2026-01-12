package com.meminzazo.ubicacionmaestra20.ui.screens.auth.recovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun RecoveryScreen(
    viewModel: RecoveryViewModel = viewModel(),
    onRecoverySuccess: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorMessage = viewModel.errorMessage

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            var email by remember { mutableStateOf("") }

            Text(
                text = "Recuperar contraseña",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(64.dp))

            TextField(
                value = email,
                onValueChange = {email = it},
                label = { Text(text = "Correo registrado") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let{
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.recovery(
                        email = email,
                        onSuccess = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Si el correo está registrado, recibirás un enlace")
                                onRecoverySuccess()
                            }
                        }
                    )
                },
                enabled = !viewModel.isLoading
            ) {
                Text(text = "Recuperar")
            }
        }
    }
    LaunchedEffect(errorMessage) {
        errorMessage?.let{
            snackbarHostState.showSnackbar(it)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecoveryScreen(
        onRecoverySuccess = {}
    )
}