package com.meminzazo.ubicacionmaestra20.ui.screens.auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AuthMenuScreen(
    onLoginClick:() -> Unit,
    onRegisterClick:() -> Unit
) {
    Scaffold{ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Ubicación Maestra",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLoginClick
            ) {
                Text(text = "Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRegisterClick
            ) {
                Text(text = "Crear cuenta")
            }
        }
    }
}
@Preview(
    //uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun AuthMenuScreenPreview() {
    AuthMenuScreen(
        onLoginClick = {},
        onRegisterClick = {}
    )
}