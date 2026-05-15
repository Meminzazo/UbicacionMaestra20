package com.meminzazo.ubicacionmaestra20.ui.screens.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meminzazo.ubicacionmaestra20.core.validators.RegisterValidator
import com.meminzazo.ubicacionmaestra20.data.repository.AuthRepository
import com.meminzazo.ubicacionmaestra20.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registerSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authrepository: AuthRepository,
    private val userRepositoy: UserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        email: String,
        password: String,
        confirmPassword: String
    ){
        val validationError = RegisterValidator.validate(email, password, confirmPassword)
        if (validationError != null){
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Paso 1 - Crear cuenta en Authentication
            authrepository.register(email, password)
                .onSuccess { uid ->
                    // Paso 2 Crear documento en Firestore
                    userRepositoy.createUserFirestore(uid, email)
                        .onSuccess {
                            Log.d("RegisterViewModel","Entrando a creacion RealtimeDB")
                            userRepositoy.createUserRealtimeDB(uid)
                                .onSuccess {
                                    _uiState.update {
                                        it.copy(
                                            isLoading = false,
                                            registerSuccess = true
                                        )
                                    }
                                }
                        }
                        .onFailure { throwable ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = throwable.message ?: "Error al registrarse"
                                )
                            }
                        }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message
                        )
                    }
                }
        }

        fun clearError(){
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
}