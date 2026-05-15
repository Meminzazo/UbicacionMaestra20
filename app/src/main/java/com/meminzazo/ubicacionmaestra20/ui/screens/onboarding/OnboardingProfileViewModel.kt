package com.meminzazo.ubicacionmaestra20.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import com.google.firebase.auth.FirebaseAuth
import com.meminzazo.ubicacionmaestra20.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class OnboardingProfileUiState(
    val nombres: String = "",
    val apellidos: String = "",
    val telefono: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class OnboardingProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingProfileUiState())
    val uiState: StateFlow<OnboardingProfileUiState> = _uiState.asStateFlow()

    fun onNombresChange(value: String){
        _uiState.update { it.copy(nombres = value, errorMessage = null) }
    }

    fun onApellidosChange(value: String){
        _uiState.update { it.copy(apellidos = value, errorMessage = null) }
    }

    fun onTelefonoChange(value: String){
        _uiState.update { it.copy(telefono = value, errorMessage = null) }
    }

    fun saveProfile(){
        if (_uiState.value.nombres.isBlank() || _uiState.value.apellidos.isBlank()){
            _uiState.update { it.copy(errorMessage = "Nombre y apellidos son obligatorios") }
            return
        }

        viewModelScope.launch{
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val uid = firebaseAuth.currentUser?.uid ?: return@launch

            userRepository.updateUserProfile(
                uid = uid,
                nombres = _uiState.value.nombres,
                apellidos = _uiState.value.apellidos,
                telefono = _uiState.value.telefono
            )
                .onSuccess {
                // AuthViewModel detectará el cambio automáticamente
                // y navegará a NeedsGroup
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Error al guardar perfil"
                        )
                    }
                }
        }
    }
}