package com.meminzazo.ubicacionmaestra20.ui.screens.auth.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meminzazo.ubicacionmaestra20.core.validators.LoginValidator
import com.meminzazo.ubicacionmaestra20.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
): ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ){
        if (isLoading) return
        Log.d("LOGIN_DEBUG", "ViewModel login() llamado")

        val validationError = LoginValidator.validate(
            email,
            password
        )

        if (validationError != null) {
            Log.d("LOGIN_DEBUG", "Error de validación: $validationError")
            errorMessage = validationError
            return
        }

        viewModelScope.launch {
            Log.d("LOGIN_DEBUG", "Corrutina iniciada")
            isLoading = true
            errorMessage = null

            val result = repository.login(email, password)

            Log.d("LOGIN_DEBUG", "Resultado Firebase: $result")

            isLoading = false

            result
                .onSuccess {
                    Log.d("LOGIN_DEBUG", "Inicio de sesión exitoso")
                    onSuccess()
                }
                .onFailure {throwable ->
                    Log.d("LOGIN_DEBUG", "Error en el inicio de sesión: ${throwable.message}")
                    errorMessage = mapLoginError(throwable)
                }
        }
    }
    private fun mapLoginError(throwable: Throwable): String {
        val message = throwable.message ?: ""
        return when{
            message.contains("The supplied auth credential is incorrect, malformed or has expired", true) ->
                "Correo electrónico y/o contraseña es incorrecta"
            message?.contains("password is invalid", true) == true ->
                "Contraseña incorrecta"
            message?.contains("no user record", true) == true ->
                "Este correo no está registrado"
            else -> "Error al iniciar sesión: ${throwable.localizedMessage}"
        }
    }
}