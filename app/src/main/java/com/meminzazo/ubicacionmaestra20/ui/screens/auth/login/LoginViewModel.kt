package com.meminzazo.ubicacionmaestra20.ui.screens.auth.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meminzazo.ubicacionmaestra20.core.validators.LoginValidator
import com.meminzazo.ubicacionmaestra20.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val  uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(
        email: String,
        password: String
    ){
        val validationError = LoginValidator.validate(email, password)
        if (validationError != null){
            _uiState.update { it.copy(errorMessage = validationError)}
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.login(email, password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = mapLoginError(throwable)
                        )
                    }
                }
        }
    }

    fun clearError(){
        _uiState.update { it.copy(errorMessage = null) }
    }
    private fun mapLoginError(throwable: Throwable): String {
        val message = throwable.message ?: ""
        return when{
            message.contains("credential is incorrect", true) ->
                "Correo electrónico y/o contraseña incorrectos"
            message.contains("no user record", true) ->
                "Este correo no está registrado"
            message.contains("network", true) ->
                "Sin conexión a internet"
            else -> "Error al iniciar sesión"
        }
    }
}