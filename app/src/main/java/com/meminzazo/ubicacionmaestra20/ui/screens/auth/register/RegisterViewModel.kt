package com.meminzazo.ubicacionmaestra20.ui.screens.auth.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meminzazo.ubicacionmaestra20.core.validators.RegisterValidator
import com.meminzazo.ubicacionmaestra20.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
): ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ){

        if (isLoading) return
        Log.d("REGISTER_ DEBUG", "ViewModel register() llamado")

        val validationError = RegisterValidator.validate(
            email,
            password,
            confirmPassword
        )

        if (validationError != null){
            errorMessage = validationError
            return
        }

        viewModelScope.launch {
            Log.d("REGISTER_ DEBUG", "Corrutina iniciada")
            isLoading = true
            errorMessage = null
            val result = repository.register(email, password)

            Log.d("REGISTER_DEBUG", "Resultado Firebase: $result")

            isLoading = false

            result
                .onSuccess {
                    Log.d("REGISTER_DEBUG", "Registro exitoso")
                    onSuccess()
                }
                .onFailure {exception ->
                    Log.d("REGISTER_DEBUG", "Error en el registro: ${exception.message}")
                    errorMessage = exception.message
                }
        }
    }
}