package com.meminzazo.ubicacionmaestra20.ui.screens.auth.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meminzazo.ubicacionmaestra20.core.validators.RecoveryValidartor
import com.meminzazo.ubicacionmaestra20.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecoveryUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emailSent: Boolean = false
)

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecoveryUiState())
    val uiState: StateFlow<RecoveryUiState> = _uiState.asStateFlow()

    fun sendRecoveryEmail(email: String) {
        val validationError = RecoveryValidartor.validate(email)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.sendPasswordResetEmail(email)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, emailSent = true) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Error al enviar el correo"
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onEmailSentHandled() {
        _uiState.update { it.copy(emailSent = false) }
    }
}