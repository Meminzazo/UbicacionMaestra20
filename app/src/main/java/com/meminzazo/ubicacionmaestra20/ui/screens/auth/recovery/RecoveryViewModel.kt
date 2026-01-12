package com.meminzazo.ubicacionmaestra20.ui.screens.auth.recovery

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meminzazo.ubicacionmaestra20.core.validators.RecoveryValidartor
import com.meminzazo.ubicacionmaestra20.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RecoveryViewModel(
    private val repository: AuthRepository = AuthRepository()
): ViewModel(){


    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set
    fun recovery(
        email: String,
        onSuccess: () -> Unit
    ) {
        if (isLoading) return
        Log.d("RECOVERY_DEBUG", "ViewModel recovery() llamado")

        val validationError = RecoveryValidartor.validate(
            email
        )

        if (validationError != null) {
            Log.d("RECOVERY_DEBUG", "Error de validación: $validationError")
            errorMessage = validationError
            return
        }

        viewModelScope.launch {
            Log.d("RECOVERY_DEBUG", "Corrutina iniciada")
            isLoading = true
            errorMessage = null

            val result = repository.sendPasswordResetEmail(email)

            isLoading = false

            result
                .onSuccess {
                    Log.d("RECOVERY_DEBUG", "Email enviado")
                    onSuccess()
                }
                .onFailure { throwable ->
                    Log.d("RECOVERY_DEBUG", "Error al enviar email: ${throwable.message}")
                }
        }
    }
}