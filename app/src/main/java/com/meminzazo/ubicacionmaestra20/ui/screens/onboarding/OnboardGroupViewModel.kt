package com.meminzazo.ubicacionmaestra20.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.meminzazo.ubicacionmaestra20.data.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardGroupUiState(
    val groupCode: String = "",
    val isLoading: Boolean = false,
    val errorMessage : String? = null,
 )

@HiltViewModel
class OnboardGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel(){
    private val _uiState = MutableStateFlow(OnboardGroupUiState())
    val uiState: StateFlow<OnboardGroupUiState> = _uiState.asStateFlow()

    fun onGroupCodeChange(value: String){
        _uiState.update { it.copy(groupCode = value, errorMessage = null) }
    }

    fun createGroup(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val uid = firebaseAuth.currentUser?.uid ?: return@launch

            groupRepository.createGroup(uid)
                .onSuccess{
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Error al crear grupo"
                        )
                    }
                }
        }
    }

    fun joinGroup(){
        if(_uiState.value.groupCode.isBlank()){
            _uiState.update { it.copy(errorMessage = "Ingresa el código del grupo") }
            return
        }

        val regex = Regex("^[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}$")
        if (!regex.matches(_uiState.value.groupCode)){
            _uiState.update { it.copy(errorMessage = "Formato inválido, debe ser XXXX-XXXX-XXXX") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val uid = firebaseAuth.currentUser?.uid ?: return@launch

            groupRepository.joinGroup(uid, _uiState.value.groupCode)
                .onSuccess{
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Error al unirse al grupo"
                        )
                    }
                }
        }
    }
}
